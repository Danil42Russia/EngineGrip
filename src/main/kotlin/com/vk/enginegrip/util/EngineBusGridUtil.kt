package com.vk.enginegrip.util

import com.intellij.database.datagrid.DataConsumer

object EngineBusGridUtil {
    fun createEDTSafeWrapper(handler: DataConsumer): DataConsumer {
        return handler
    }
}
