package com.vk.enginegrip.editor.table

import com.intellij.database.datagrid.DataConsumer
import com.intellij.database.datagrid.DataGridListModel
import com.intellij.database.datagrid.GridColumn
import com.intellij.database.datagrid.GridDataHookUpBase
import com.intellij.database.datagrid.GridLoader
import com.intellij.database.datagrid.GridModel
import com.intellij.database.datagrid.GridPagingModel
import com.intellij.database.datagrid.GridPagingModelImpl
import com.intellij.database.datagrid.GridRequestSource
import com.intellij.database.datagrid.GridRow
import com.intellij.database.run.ui.grid.GridMutationModel
import com.intellij.database.run.ui.grid.GridStorageAndModelUpdater
import com.intellij.database.run.ui.grid.editors.GridCellEditorHelper
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus
import com.vk.enginegrip.bus.EngineBusTopics
import com.vk.enginegrip.task.BackgroundTask
import java.awt.EventQueue

class EngineGridDataHookUp(project: Project, val messageBus: MessageBus) :
    GridDataHookUpBase<GridRow, GridColumn>(project) {
    private val myModel: DataGridListModel
    private val myMutationModel: GridMutationModel
    private val myModelUpdater: GridStorageAndModelUpdater
    private val myPageModel: GridPagingModel<GridRow, GridColumn>

    private val myLoader: EngineGridLoader

    init {
        myModel = DataGridListModel { v1: Any, v2: Any -> GridCellEditorHelper.areValuesEqual(v1, v2) }
        myMutationModel = GridMutationModel(this)
        myModelUpdater = GridStorageAndModelUpdater(myModel, myMutationModel, null)
        myPageModel = GridPagingModelImpl.SinglePage<GridRow, GridColumn>(myMutationModel)

        myLoader = EngineGridLoader()
    }

    override fun getPageModel(): GridPagingModel<GridRow, GridColumn> = myPageModel

    override fun getLoader(): GridLoader = myLoader

    override fun getMutationModel(): GridModel<GridRow, GridColumn> = myMutationModel

    override fun getDataModel(): GridModel<GridRow, GridColumn> = myModel

    private inner class EngineGridLoader : GridLoader {
        override fun reloadCurrentPage(source: GridRequestSource) {
            load(source, 0)
        }

        override fun loadNextPage(source: GridRequestSource) {
            load(source, 0)
        }

        override fun loadPreviousPage(source: GridRequestSource) {
            load(source, 0)
        }

        override fun loadLastPage(source: GridRequestSource) {
            load(source, 0)
        }

        override fun loadFirstPage(source: GridRequestSource) {
//           мы открыли страницу
            load(source, 0)
        }

        override fun load(source: GridRequestSource, offset: Int) {
            doLoadData(source)
        }

        override fun updateTotalRowCount(source: GridRequestSource) {
            // TODO
        }

        override fun applyFilterAndSorting(source: GridRequestSource) {
            // TODO
        }

        override fun updateIsTotalRowCountUpdateable() {
            // TODO
        }

        private fun doLoadData(source: GridRequestSource) {
            val progressListener = messageBus.syncPublisher(EngineBusTopics.Companion.PROGRESS_TOPIC)

            BackgroundTask.runTask(project, "Querying request") {
                progressListener.taskStarted()

                val columns = listOf<GridColumn>(
                    DataConsumer.Column(0, "Column Name", 1, null, null),
                    DataConsumer.Column(0, "Column Value", 1, null, null),
                )
                val rows = listOf<Array<Any>>(
                    arrayOf("1", "2"),
                    arrayOf("2", "3"),
                    arrayOf("3", "1"),
                    arrayOf("string", "value"),
                    arrayOf("d", "v"),
                    arrayOf("d", "1"),
                    arrayOf("j", "{\"str\":1}"),
                ).mapIndexed { index, row -> DataConsumer.Row.create(index, row) }

                progressListener.sendingRequest()
                Thread.sleep(2_000) // Эмуляция http

                progressListener.processingRequest()
                Thread.sleep(1_000) // Эмуляция парсинга

                EventQueue.invokeLater {
                    myModelUpdater.removeRows(0, myModel.rowCount)
                    myModelUpdater.setColumns(columns)
                    myModelUpdater.addRows(rows)
                }

                progressListener.taskFinished()
            }
        }
    }
}
