package com.vk.enginegrip.enigne

import com.intellij.openapi.components.BaseState
import com.intellij.util.xmlb.annotations.Tag
import com.intellij.util.xmlb.annotations.XCollection

class EngineConnectionState : BaseState() {
    @get:XCollection
    @get:Tag("connections")
    var connections: MutableList<EngineTestActorConnection> by list()
}
