package com.vk.enginegrip.editor.table

import com.intellij.database.DatabaseDataKeys
import com.intellij.database.data.types.BaseConversionGraph
import com.intellij.database.datagrid.*
import com.intellij.database.editor.TableFileEditor
import com.intellij.database.extractors.BaseObjectFormatter
import com.intellij.database.extractors.FormatterCreator
import com.intellij.database.run.actions.enablePagination
import com.intellij.database.run.ui.grid.editors.*
import com.intellij.database.run.ui.grid.renderers.DefaultBooleanRendererFactory
import com.intellij.database.run.ui.grid.renderers.DefaultNumericRendererFactory
import com.intellij.database.run.ui.grid.renderers.DefaultTextRendererFactory
import com.intellij.database.run.ui.grid.renderers.GridCellRendererFactories
import com.intellij.database.settings.DataGridAppearanceSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.vk.enginegrip.editor.EngineEditorProvider.Companion.ENGINE_CONNECTION
import com.vk.enginegrip.settings.EngineSettings
import com.vk.enginegrip.util.EngineGridUtil

class EngineTableFileEditor(project: Project, file: VirtualFile) : TableFileEditor(project, file) {
    private val grid: DataGrid

    init {
        grid = createTable()
    }

    private fun createTable(): DataGrid {
        val connection = file.getUserData(ENGINE_CONNECTION)!! // TODO: NPE
        val messageBus = project.messageBus

        val hookUp = EngineGridDataHookUp(project, messageBus, connection)
        val grid = createDataGrid(hookUp)

        GridUtil.addGridHeaderComponent(grid)
        EngineGridUtil.setupProgressIndicating(grid, messageBus)

        return grid
    }

    override fun configure(grid: DataGrid, appearance: DataGridAppearance) {
        GridCellEditorHelper.set(grid, GridCellEditorHelperImpl())
        GridHelper.set(grid, GridHelperImpl())
        GridCellEditorFactoryProvider.set(grid, GridCellEditorFactoryImpl.getInstance())
        val factories = listOf(
            DefaultBooleanRendererFactory(grid),
            DefaultNumericRendererFactory(grid),
            DefaultTextRendererFactory(grid)
        )
        GridCellRendererFactories.set(grid, GridCellRendererFactories(factories))
        val formatter = BaseObjectFormatter()
        grid.setObjectFormatterProvider { _ -> formatter }
        BaseConversionGraph.set(
            grid,
            BaseConversionGraph(FormatsCache(), FormatterCreator.get(grid)) { grid.objectFormatter }
        )
        appearance.setResultViewShowRowNumbers(true);
        appearance.booleanMode = DataGridAppearanceSettings.getSettings().booleanMode;

        grid.putUserData(DatabaseDataKeys.DATA_GRID_SETTINGS_KEY, EngineSettings.getSettings())

        enablePagination(grid, true, null)
    }

    override fun getDataGrid(): DataGrid = grid
}
