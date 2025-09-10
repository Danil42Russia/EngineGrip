package com.vk.enginegrip.editor.table

import com.intellij.database.datagrid.DataGrid
import com.intellij.database.datagrid.DataProducer
import com.intellij.database.datagrid.GridHelperImpl
import com.intellij.database.dump.BaseGridHandler
import com.intellij.database.dump.DumpHandler
import com.intellij.database.dump.ExtractionHelper
import com.intellij.database.extractors.DataExtractorFactory
import com.intellij.database.extractors.ExtractionConfig
import com.intellij.database.run.actions.DumpSource
import com.intellij.database.run.actions.DumpSource.DataGridSource

class EngineGridHelper : GridHelperImpl() {
    override fun createDumpHandler(
        source: DumpSource<*>,
        manager: ExtractionHelper,
        factory: DataExtractorFactory,
        config: ExtractionConfig
    ): DumpHandler<*> {
        val gridSource = source as DataGridSource
        val grid = gridSource.grid

        return object : BaseGridHandler(grid.project, grid, gridSource.nameProvider, manager, factory, config) {
            override fun createProducer(grid: DataGrid, index: Int): DataProducer {
                return EngineDataProducer()
            }
        }
    }
}
