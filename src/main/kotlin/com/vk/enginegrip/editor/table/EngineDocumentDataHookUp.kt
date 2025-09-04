package com.vk.enginegrip.editor.table

import com.intellij.database.datagrid.DataConsumer
import com.intellij.database.datagrid.DocumentDataHookUp
import com.intellij.database.datagrid.GridColumn
import com.intellij.database.datagrid.GridRequestSource
import com.intellij.database.datagrid.GridRow
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project

class EngineDocumentDataHookUp(project: Project, document: Document) :
    DocumentDataHookUp(project, document, null) {
    override fun buildMarkup(
        sequence: CharSequence,
        myRequestSource: GridRequestSource
    ): DataMarkup {

        val columns = listOf<GridColumn>(
            DataConsumer.Column(0, "Column Name", 1, null, null),
            DataConsumer.Column(0, "Column Value", 1, null, null),
        )
        val rows = listOf<GridRow>(
            DataConsumer.Row.create(0, arrayOf("1", "1")),
            DataConsumer.Row.create(2, arrayOf("2", "2")),
        )

        return EngineDataMarkup(columns, rows)
    }

}
