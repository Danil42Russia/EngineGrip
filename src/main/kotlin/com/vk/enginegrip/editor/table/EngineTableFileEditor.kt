package com.vk.enginegrip.editor.table

import com.intellij.database.data.types.BaseConversionGraph
import com.intellij.database.datagrid.DataGrid
import com.intellij.database.datagrid.DataGridAppearance
import com.intellij.database.datagrid.GridHelper
import com.intellij.database.datagrid.GridHelperImpl
import com.intellij.database.editor.TableFileEditor
import com.intellij.database.extractors.BaseObjectFormatter
import com.intellij.database.extractors.FormatterCreator
import com.intellij.database.run.ui.grid.editors.*
import com.intellij.database.run.ui.grid.renderers.DefaultBooleanRendererFactory
import com.intellij.database.run.ui.grid.renderers.DefaultNumericRendererFactory
import com.intellij.database.run.ui.grid.renderers.DefaultTextRendererFactory
import com.intellij.database.run.ui.grid.renderers.GridCellRendererFactories
import com.intellij.database.settings.DataGridAppearanceSettings
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.vk.enginegrip.editor.table_mvp.EngineGridDataHookUp

class EngineTableFileEditor(project: Project, file: VirtualFile) :
    TableFileEditor(project, file) {
    private val grid: DataGrid

    init {
        grid = createTable()
    }

    private fun createTable(): DataGrid {
        if (false) {
            val document = EditorFactory.getInstance().createDocument("")
            val hookUp = EngineDocumentDataHookUp(project, document)
        }

        val hookUp = EngineGridDataHookUp(project)

//        val helper = EngineGridHelper()
//        setPageSize(hookUp, helper)

//        val grid = TableResultPanel(
//            project,
//            hookUp,
//            GridUtil.getGridPopupActions()
//        ) { grid, appearance ->
//            GridHelper.set(grid, helper)
//        }
//        hookUp.loader.loadFirstPage(object : GridRequestSource(grid) {})
//

        val grid = createDataGrid(hookUp)
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
    }

    override fun getDataGrid(): DataGrid = grid
}
