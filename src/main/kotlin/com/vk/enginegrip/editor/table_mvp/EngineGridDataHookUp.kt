package com.vk.enginegrip.editor.table_mvp

import com.intellij.database.datagrid.*
import com.intellij.database.run.ui.grid.GridMutationModel
import com.intellij.database.run.ui.grid.GridStorageAndModelUpdater
import com.intellij.database.run.ui.grid.editors.GridCellEditorHelper
import com.intellij.openapi.project.Project
import com.vk.enginegrip.bus.EngineBus
import com.vk.enginegrip.util.EngineBusGridUtil

class EngineGridDataHookUp(project: Project, messageBus: EngineBus.Consuming) :
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

//        messageBus.addConsumer(EngineBusGridUtil.createEDTSafeWrapper(this.myLoader))
    }

    override fun getPageModel(): GridPagingModel<GridRow, GridColumn> = myPageModel

    override fun getLoader(): GridLoader = myLoader

    override fun getMutationModel(): GridModel<GridRow, GridColumn> = myMutationModel

    override fun getDataModel(): GridModel<GridRow, GridColumn> = myModel


    private inner class EngineGridLoader : DataConsumer, GridLoader {
        val columns: List<GridColumn>
        val rows: List<GridRow>

        init {
            columns = listOf<GridColumn>(
                DataConsumer.Column(0, "Column Name", 1, null, null),
                DataConsumer.Column(0, "Column Value", 1, null, null),
            )
            rows = listOf<Array<Any>>(
                arrayOf("1", "2"),
                arrayOf("2", "3"),
                arrayOf("3", "1"),
                arrayOf("string", "value"),
                arrayOf("d", "v"),
                arrayOf("d", "1"),
                arrayOf("j", "{\"str\":1}"),
            ).mapIndexed { index, row -> DataConsumer.Row.create(index, row) }
        }


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

        override fun addRows(
            context: GridDataRequest.Context,
            rows: List<GridRow?>
        ) {
            super.addRows(context, rows)
        }

        override fun setColumns(
            context: GridDataRequest.Context,
            columns: Array<out GridColumn?>
        ) {
            super.setColumns(context, columns)
        }

        override fun setColumns(
            context: GridDataRequest.Context,
            subQueryIndex: Int,
            resultSetIndex: Int,
            columns: Array<out GridColumn?>,
            firstRowNum: Int
        ) {
            super.setColumns(context, subQueryIndex, resultSetIndex, columns, firstRowNum)
        }

        override fun setInReference(
            context: GridDataRequest.Context,
            reference: Any
        ) {
            super.setInReference(context, reference)
        }

        override fun updateColumns(
            context: GridDataRequest.Context,
            columns: Array<out GridColumn?>
        ) {
            super.updateColumns(context, columns)
        }

        override fun setOutReferences(
            context: GridDataRequest.Context,
            references: Set<Any?>
        ) {
            super.setOutReferences(context, references)
        }

        override fun afterLastRowAdded(
            context: GridDataRequest.Context,
            total: Int
        ) {
            super.afterLastRowAdded(context, total)
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
//            тут мы читаем
//            EngineUiUtil.setProgressMessage(this)
            myModelUpdater.removeRows(0, myModel.rowCount)
            myModelUpdater.setColumns(columns)
            myModelUpdater.addRows(rows)
        }
    }
}
