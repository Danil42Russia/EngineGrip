package com.vk.enginegrip.editor.table_mvp

import com.intellij.database.datagrid.GridDataRequest
import com.intellij.openapi.util.UserDataHolderBase
import org.jetbrains.concurrency.AsyncPromise

class EngineDataRequest : UserDataHolderBase(), GridDataRequest {
    private val promise = AsyncPromise<Void>();

    override fun getPromise(): AsyncPromise<Void> {
        return promise
    }


}
