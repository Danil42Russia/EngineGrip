package com.vk.enginegrip.enigne

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.ApiStatus.Internal

@Service(Service.Level.PROJECT)
@State(
    name = "engineConnectionStorage",
    storages = [
        Storage(
            value = "engineConnection.xml",
            roamingType = RoamingType.DISABLED
        )
    ]
)
class EngineConnectionDataStorage : PersistentStateComponent<EngineConnectionState> {
    private var state = EngineConnectionState()

    @Internal
    override fun getState(): EngineConnectionState = state

    @Internal
    override fun loadState(state: EngineConnectionState) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    fun getActors(): List<EngineActor> {
        return state.connections.map {
            EngineActor(
                name = (it.name ?: ""),
                connection = EngineActorConnection(
                    url = (it.url ?: ""),
                    port = it.port,
                    actor = it.actorID
                ),
            )
        }
    }

    fun addInnerActor(actor: EngineActor) {
        val inner = EngineTestActorConnection().also {
            it.name = actor.name
            it.actorID = actor.connection.actor
            it.url = actor.connection.url
        }

        state.connections.add(inner)
    }

    fun removeInnerActor(actor: EngineActor) {
        val inner = EngineTestActorConnection().also {
            it.name = actor.name
            it.actorID = actor.connection.actor
            it.url = actor.connection.url
        }

        if (state.connections.contains(inner)) {
            state.connections.remove(inner)
        } else {
            // TODO: logger
            println("!!! ошибка в логике удаления элемента из стейта: $actor")
        }
    }

    companion object {
        fun getInstance(project: Project): EngineConnectionDataStorage = project.service()
    }
}
