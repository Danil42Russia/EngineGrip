package com.vk.enginegrip.editor.table_mvp.bus

import com.intellij.database.datagrid.DataConsumer
import com.intellij.database.datagrid.DataProducer
import com.intellij.database.datagrid.GridDataRequest
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus
import com.vk.enginegrip.bus.EngineBus
import com.vk.enginegrip.bus.EngineBusTopics

class ZalupaRequestBroker : EngineBus.Producing, EngineBus.Consuming {
    private val myMessageBus: MessageBus

    private constructor(messageBus: MessageBus) {
        myMessageBus = messageBus

        val connection = messageBus.connect()
        connection.subscribe(EngineBusTopics.REQUEST_TOPIC, object : DataProducer {
            override fun processRequest(request: GridDataRequest) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        fun newInstance(project: Project): ZalupaRequestBroker {
            return ZalupaRequestBroker(project.messageBus)
        }
    }

    fun consumerFor(): EngineBus.Consuming {
        return this
    }

    override fun addConsumer(consumer: DataConsumer) {
        val connection = myMessageBus.connect()
        connection.subscribe(EngineBusTopics.RESPONSE_TOPIC, consumer)
    }

}
