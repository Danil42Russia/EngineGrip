package com.vk.enginegrip.bus

import com.intellij.util.messages.Topic
import com.intellij.util.messages.Topic.BroadcastDirection

interface EngineBusTopics {
    companion object {
        val PROGRESS_TOPIC = create(EngineProgressListener::class.java, BroadcastDirection.TO_PARENT, true)

        private fun <T> create(
            listenerClass: Class<T>,
            broadcastDirection: BroadcastDirection,
            immediateDelivery: Boolean,
        ): Topic<T> {
            return Topic<T>(listenerClass, broadcastDirection, immediateDelivery)
        }
    }
}
