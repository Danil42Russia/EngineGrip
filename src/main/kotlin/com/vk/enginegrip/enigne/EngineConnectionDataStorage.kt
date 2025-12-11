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
class EngineConnectionDataStorage : PersistentStateComponent<EngineConnectionsState> {
    private var state = EngineConnectionsState()

    @Internal
    override fun getState(): EngineConnectionsState = state

    @Internal
    override fun loadState(state: EngineConnectionsState) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    fun getActors(): List<EngineActor> {
        return state.connections.map { it.fromState() }
    }

    fun addInnerActor(actor: EngineActor) {
        val inner = actor.toState()

        state.connections.add(inner)
    }

    fun removeInnerActor(actor: EngineActor) {
        val inner = actor.toState()

        if (state.connections.contains(inner)) {
            state.connections.remove(inner)
        } else {
            // TODO: logger
            println("!!! ошибка в логике удаления элемента из стейта: $actor")
        }
    }

    fun EngineActor.toState(): EngineConnectionState {
        return EngineConnectionState().also {
            it.name = name
            it.actorID = connection.actor
            it.url = connection.url
            it.port = connection.port
        }
    }

    fun EngineConnectionState.fromState(): EngineActor {
        return EngineActor(
            name = (name ?: ""),
            connection = EngineActorConnection(
                url = (url ?: ""),
                port = port,
                actor = actorID
            ),
        )
    }

    companion object {
        fun getInstance(project: Project): EngineConnectionDataStorage = project.service()
    }
}
