package com.vk.enginegrip.editor.table

import com.intellij.database.datagrid.DocumentDataHookUp
import com.intellij.database.datagrid.GridColumn
import com.intellij.database.datagrid.GridRow
import com.intellij.database.datagrid.ModelIndex
import com.intellij.database.datagrid.mutating.RowMutation

class EngineDataMarkup(columns: List<GridColumn>, rows: List<GridRow>) : DocumentDataHookUp.DataMarkup(columns, rows) {
    override fun deleteRows(session: DocumentDataHookUp.UpdateSession, sortedRows: List<GridRow?>): Boolean {
        return false
    }

    override fun insertRow(session: DocumentDataHookUp.UpdateSession): Boolean {
        return false
    }

    override fun cloneRow(session: DocumentDataHookUp.UpdateSession, row: GridRow): Boolean {
        return false
    }

    override fun deleteColumns(session: DocumentDataHookUp.UpdateSession, sortedColumns: List<GridColumn?>): Boolean {
        return false
    }

    override fun insertColumn(session: DocumentDataHookUp.UpdateSession, name: String?): Boolean {
        return false
    }

    override fun cloneColumn(session: DocumentDataHookUp.UpdateSession, column: GridColumn): Boolean {
        return false
    }

    override fun update(session: DocumentDataHookUp.UpdateSession, infos: List<RowMutation?>): Boolean {
        return false
    }

    override fun renameColumn(
        session: DocumentDataHookUp.UpdateSession,
        column: ModelIndex<GridColumn?>,
        name: String,
    ): Boolean {
        return false
    }
}
