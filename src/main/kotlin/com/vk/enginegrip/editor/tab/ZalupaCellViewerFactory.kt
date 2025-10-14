package com.vk.enginegrip.editor.tab

import com.intellij.database.datagrid.DataGrid
import com.intellij.database.datagrid.GridColumn
import com.intellij.database.datagrid.GridRow
import com.intellij.database.datagrid.ModelIndex
import com.intellij.database.run.ui.CellViewer
import com.intellij.database.run.ui.CellViewerFactory
import com.intellij.database.run.ui.EditorCellViewer
import com.intellij.database.run.ui.Suitability

object ZalupaCellViewerFactory : CellViewerFactory {
    override fun getSuitability(grid: DataGrid, row: ModelIndex<GridRow>, column: ModelIndex<GridColumn>): Suitability {
        return Suitability.MAX
    }

    override fun createViewer(grid: DataGrid): CellViewer {
        return ZalupaCellViewer(grid.project, grid)
        return EditorCellViewer(grid.project, grid, true)
    }
}
