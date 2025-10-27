package com.vk.enginegrip.bus

import com.intellij.util.messages.Topic
import com.vk.enginegrip.enigne.EngineActor

interface EngineActorTopics {
    companion object {
        val TOPIC = Topic.create("EngineActorTopics", EngineActorTopics::class.java)
    }

    fun onNewActor(actor: EngineActor) {}
}
