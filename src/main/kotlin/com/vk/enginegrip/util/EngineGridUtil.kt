package com.vk.enginegrip.util

import com.intellij.database.datagrid.DataGrid
import com.intellij.util.messages.MessageBus
import com.vk.enginegrip.bus.EngineBusTopics
import com.vk.enginegrip.bus.EngineProgressListener

object EngineGridUtil {
    fun setupProgressIndicating(grid: DataGrid, messageBus: MessageBus) {
        // TODO: посмотреть на grid.dataHookup

        messageBus.connect().subscribe(EngineBusTopics.PROGRESS_TOPIC, object : EngineProgressListener {
            override fun taskStarted() {
                EngineUiUtil.setProgressMessage(grid, "Начало запроса")
            }

            override fun sendingRequest() {
                EngineUiUtil.setProgressMessage(grid, "Отправка запроса")
            }

            override fun processingRequest() {
                EngineUiUtil.setProgressMessage(grid, "Обработка запроса")
            }

            override fun taskFinished() {
                EngineUiUtil.setProgressMessage(grid, null)
            }
        })
    }
}
