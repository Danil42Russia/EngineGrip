package com.vk.enginegrip.editor.tab

import com.intellij.database.datagrid.DataGrid
import com.intellij.database.run.ui.*
import com.intellij.openapi.util.Disposer

class UnserializedValueEditorTab : ValueEditorTab {
    override val priority: Int = 50

    override fun createTabInfoProvider(
        grid: DataGrid,
        openValueEditorTab: () -> Unit
    ): TabInfoProvider {
        return RecordViewInfoProvider(grid, openValueEditorTab)
    }

    private class RecordViewInfoProvider(private val grid: DataGrid, openValueEditorTab: () -> Unit) : TabInfoProvider(
        "Unserialized",
        null
    ) {
        private var viewer: CellViewer = UnserializedCellViewerFactory.createViewer(grid)

        init {
            updateTabInfo()
        }

        override fun dispose() {
            Disposer.dispose(viewer)
        }

        override fun getViewer(): CellViewer = viewer
    }
}
