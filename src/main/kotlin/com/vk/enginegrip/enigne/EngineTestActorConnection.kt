package com.vk.enginegrip.enigne

import com.intellij.openapi.components.BaseState
import com.intellij.util.xmlb.annotations.Tag

@Tag("engine-connection")
class EngineTestActorConnection : BaseState() {
    @get:Tag("name")
    var name: String? by string()

    @get:Tag("actor-id")
    var actorID: Int by property(defaultValue = 0)

    @get:Tag("url")
    var url: String? by string()
}
