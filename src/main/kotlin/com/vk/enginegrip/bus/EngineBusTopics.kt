package com.vk.enginegrip.bus

import com.intellij.database.datagrid.DataConsumer
import com.intellij.database.datagrid.DataProducer
import com.intellij.util.messages.Topic
import com.intellij.util.messages.Topic.BroadcastDirection

interface EngineBusTopics {
    companion object {
        val PRODUCER_TOPIC: Topic<DataProducer> = create(DataProducer::class.java, BroadcastDirection.TO_PARENT, true)
        val CONSUMER_TOPIC: Topic<DataConsumer> = create(DataConsumer::class.java, BroadcastDirection.TO_PARENT, true)

        val REQUEST_TOPIC = PRODUCER_TOPIC
        val RESPONSE_TOPIC = CONSUMER_TOPIC

        private fun <T> create(
            listenerClass: Class<T>,
            broadcastDirection: BroadcastDirection,
            immediateDelivery: Boolean,
        ): Topic<T> {
            return Topic<T>(listenerClass, broadcastDirection, immediateDelivery)
        }
    }
}
