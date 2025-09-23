package com.vk.enginegrip.bus

import com.intellij.database.datagrid.DataConsumer
import com.intellij.database.datagrid.DataProducer
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.EventDispatcher
import com.intellij.util.messages.MessageBusFactory
import com.intellij.util.messages.MessageBusOwner
import com.intellij.util.messages.impl.PluginListenerDescriptor

@Service(Service.Level.PROJECT)
class EngineBus(project: Project) {

    init {
        val messageBus = MessageBusFactory.newMessageBus(object : MessageBusOwner {
            override fun createListener(descriptor: PluginListenerDescriptor): Any {
                TODO("Not yet implemented")
            }

            override fun isDisposed(): Boolean = project.isDisposed
        })

        val connection = messageBus.connect()

//        connection.subscribe(
//            EngineBusTopics.CONSUMER_TOPIC, EventDispatcher.createMulticaster(
//                DataConsumer::class.java
//            ) { emptyList() }
//        )
//
//        connection.subscribe(
//            EngineBusTopics.PRODUCER_TOPIC, EventDispatcher.createMulticaster(
//                DataProducer::class.java
//            ) { emptyList() }
//        )

    }

    /**
     * В этого мы пишем
     */
    public interface Producing {}

    /**
     * Их этого мы читаем
     */
    public interface Consuming {
        fun addConsumer(consumer: DataConsumer)
    }
}
