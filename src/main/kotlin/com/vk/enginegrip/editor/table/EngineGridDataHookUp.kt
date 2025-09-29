package com.vk.enginegrip.editor.table

import com.intellij.database.datagrid.*
import com.intellij.database.run.ui.grid.GridMutationModel
import com.intellij.database.run.ui.grid.GridStorageAndModelUpdater
import com.intellij.database.run.ui.grid.editors.GridCellEditorHelper
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus

class EngineGridDataHookUp(project: Project, val messageBus: MessageBus) :
    GridDataHookUpBase<GridRow, GridColumn>(project) {
    private val myModel: DataGridListModel
    private val myMutationModel: GridMutationModel
    private val myModelUpdater: GridStorageAndModelUpdater
    private val myPageModel: MultiPageModelImpl<GridRow, GridColumn>

    private val myLoader: EngineGridLoader

    init {
        myModel = DataGridListModel { v1: Any, v2: Any -> GridCellEditorHelper.areValuesEqual(v1, v2) }
        myMutationModel = GridMutationModel(this)
        myModelUpdater = GridStorageAndModelUpdater(myModel, myMutationModel, null)
//        myPageModel = GridPagingModelImpl.SinglePage<GridRow, GridColumn>(myMutationModel)
        myPageModel = MultiPageModelImpl(myModel, null)


        myLoader = EngineGridLoader()
    }

    override fun getPageModel(): GridPagingModel<GridRow, GridColumn> = myPageModel

    override fun getLoader(): GridLoader = myLoader

    override fun getMutationModel(): GridModel<GridRow, GridColumn> = myMutationModel

    override fun getDataModel(): GridModel<GridRow, GridColumn> = myModel

    private inner class EngineGridLoader : GridLoader {
        val columns: List<GridColumn>

//        private var myRowsLoaded = -1
        private val pageSize = 100
        private val totalPageCount = 3
        private var total = (pageSize * totalPageCount).toLong() // 300

        private var currentPage = -1

        init {
            columns = listOf<GridColumn>(
                DataConsumer.Column(0, "Column Name", 1, null, null),
                DataConsumer.Column(0, "Column Value", 1, null, null),
            )
        }

        override fun reloadCurrentPage(source: GridRequestSource) {
            println("!!! reloadCurrentPage")
            doLoadData(source)
        }

        override fun loadNextPage(source: GridRequestSource) {
            println("!!! loadNextPage")
            currentPage += 1
            doLoadData(source)
        }

        override fun loadPreviousPage(source: GridRequestSource) {
            println("!!! loadPreviousPage")
            currentPage -= 1
            doLoadData(source)
        }

        override fun loadLastPage(source: GridRequestSource) {
            println("!!! loadLastPage")
            currentPage = (totalPageCount - 1)
            doLoadData(source)
        }

        // мы открыли страницу
        override fun loadFirstPage(source: GridRequestSource) {
            currentPage = 0

            println("!!! loadFirstPage")
            doLoadData(source)
        }

        override fun load(source: GridRequestSource, offset: Int) {
            println("!!! load")
            TODO()
        }

        override fun updateTotalRowCount(source: GridRequestSource) {
            println("!!! updateTotalRowCount")
            TODO()
        }

        override fun applyFilterAndSorting(source: GridRequestSource) {
            println("!!! applyFilterAndSorting")
            TODO()
        }

        override fun updateIsTotalRowCountUpdateable() {
            println("!!! updateIsTotalRowCountUpdateable")
            TODO()
        }

        private fun doLoadData(source: GridRequestSource) {
            println("!!! doLoadData")

            myPageModel.setTotalRowCount(total, true)
            myPageModel.pageSize = pageSize


            val rows: List<GridRow> = ((pageSize * currentPage)..((pageSize) * (currentPage+ 1))).map { index ->
                DataConsumer.Row.create(index, arrayOf("+$index", "-$index"))
            }

//            if (currentPage == 0) {
                val pageStart = rows[0].rowNum
                myPageModel.pageStart = pageStart
//            }

            val pageEnd = rows[rows.size - 1].rowNum
            myPageModel.pageEnd = pageEnd

            myModelUpdater.removeRows(0, myModel.rowCount)
            myModelUpdater.setColumns(columns)
            myModelUpdater.addRows(rows)

//            myRowsLoaded += rows.size

//            myPageModel.pageSize = 100 //
//            myPageModel.pageStart = offset
//            myPageModel.pageEnd = 3 // Сколько всего страниц
//
            source.requestComplete(true)
        }
    }
}
