package com.vk.enginegrip.http

import com.intellij.execution.process.ProcessIOExecutorService
import io.ktor.util.decodeBase64String
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

@Serializable
data class WildcardPaginationParams(
    val limit: Int,

    @SerialName("after_key_suffix")
    val afterKeySuffix: String,
)

@Serializable
data class WildcardDictWithPaginationParams(
    val prefix: String,

    @SerialName("fields_mask")
    val fieldsMask: Int,
    val limit: WildcardPaginationParams,
)

@Serializable
data class WildcardDictEntry(
    val value: String,
    val flags: Int
)

object WildcardDictEntrySerializer :
    JsonTransformingSerializer<Map<String, WildcardDictEntry>>(
        MapSerializer(
            String.serializer(),
            WildcardDictEntry.serializer()
        )
    ) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val obj = JsonObject((element as JsonObject).mapValues {
            val v = it.value as JsonObject
            val vValue = v["value"]
            if (vValue is JsonObject) {
                val content = (vValue["base64"] as JsonPrimitive).content.decodeBase64String()

                JsonObject(
                    mapOf(
                        "value" to JsonPrimitive(content),
                        "flags" to v["flags"]!!,
                    )
                )
            } else {
                v
            }
        })
        return obj
    }

    override fun transformSerialize(element: JsonElement): JsonElement = element
}


@Serializable
data class WildcardResult(
    @Serializable(with = WildcardDictEntrySerializer::class)
    val result: Map<String, WildcardDictEntry> = mapOf(),

    @SerialName("has_more_items")
    val hasMoreItems: Boolean = false,
)

@Serializable
data class WildcardCountRequest(val prefix: String)

@Serializable
data class WildcardCountResponse(val count: Int)


class EngineJRPClient(private val baseUrl: String, private val actorId: Int) {


    private fun createHttpClient(): HttpClient {
        return HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .executor(ProcessIOExecutorService.INSTANCE)
            .build()
    }

    private fun <Request, Response> abstractRequest(
        methodName: String,
        request: Request,
        requestSerialize: KSerializer<Request>,
        responseSerialize: KSerializer<Response>,
    ): Response? {
        println("request: $request")
        val httpClient = createHttpClient()

        val jsonParms: String
        try {
            jsonParms = Json.encodeToString(requestSerialize, value = request)
        } catch (e: Exception) {
            println("error: $e")
            return null
        }

        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseUrl/$methodName?actor=$actorId"))
            .POST(HttpRequest.BodyPublishers.ofString(jsonParms))
            .timeout(Duration.ofSeconds(10))
            .build()

        val httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (httpResponse.statusCode() != HttpURLConnection.HTTP_OK) {
            println("statusCode: ${httpResponse.statusCode()}")
            return null
        }

        var responseBody = httpResponse.body()
        if (!(responseBody.startsWith("{") && responseBody.endsWith("}"))) {
            val elementName = responseSerialize.descriptor.getElementName(0)
            responseBody = """{"$elementName": $responseBody}""".trimIndent()
        }


        val response: Response
        try {
            val format = Json { }
            response = format.decodeFromString(deserializer = responseSerialize, string = responseBody)
        } catch (e: Exception) {
            println("error: $e")
            return null
        }

        println("response: $response")
        return response
    }

    fun getWildcardDictWithPagination(prefix: String, limit: WildcardPaginationParams): WildcardResult? {
        val param = WildcardDictWithPaginationParams(
            prefix = prefix,
            fieldsMask = 3,
            limit = limit,
        )

        return abstractRequest(
            methodName = "memcache.getWildcardDictWithPagination",
            request = param,
            requestSerialize = WildcardDictWithPaginationParams.serializer(),
            responseSerialize = WildcardResult.serializer(),
        )
    }

    fun getWildcardCount(prefix: String): WildcardCountResponse? {
        val request = WildcardCountRequest(prefix)

        return abstractRequest(
            methodName = "memcache.getWildcardCount",
            request = request,
            requestSerialize = WildcardCountRequest.serializer(),
            responseSerialize = WildcardCountResponse.serializer(),
        )
    }
}
