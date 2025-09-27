package com.vk.enginegrip.http

import com.intellij.execution.process.ProcessIOExecutorService
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import java.net.HttpURLConnection
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

@Serializable
data class ConfdataValue(val key: String, val value: String)

object EngineJRPClient {
    private const val JRP_BASE_URL = "http://localhost:8100"

    private fun createHttpClient(): HttpClient {
        return HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .executor(ProcessIOExecutorService.INSTANCE)
            .build()
    }

    fun getWildcardDict(actorId: Int): List<ConfdataValue>? {
        val httpClient = createHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$JRP_BASE_URL/memcache.getWildcard?actor=$actorId"))
            .POST(HttpRequest.BodyPublishers.noBody())
            .timeout(Duration.ofSeconds(10))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            return null
        }

        val res = Json.decodeFromString<List<ConfdataValue>>(response.body())
        return res
    }
}
