package com.vk.enginegrip.startup

import com.intellij.database.datagrid.DataProducer
import com.intellij.database.datagrid.GridDataRequest
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.vk.enginegrip.bus.EngineBusTopics

class EngineProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        val connection = project.messageBus.connect()

//        connection.subscribe(EngineBusTopics.REQUEST_TOPIC, object : DataProducer {
//            override fun processRequest(request: GridDataRequest) {
//                TODO("Not yet implemented")
//            }
//        })
    }
}
