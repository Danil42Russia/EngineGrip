package com.vk.enginegrip.enigne

import com.intellij.openapi.components.BaseState
import com.intellij.util.xmlb.annotations.Tag

// TODO: вынести в интерфейс поля
@Tag("engine-connection")
class EngineConnectionState : BaseState() {
    @get:Tag("name")
    var name: String? by string()

    @get:Tag("actor-id")
    var actorID: Int by property(defaultValue = 0)

    @get:Tag("url")
    var url: String? by string()

    @get:Tag("port")
    var port: Int by property(defaultValue = 0)
}
