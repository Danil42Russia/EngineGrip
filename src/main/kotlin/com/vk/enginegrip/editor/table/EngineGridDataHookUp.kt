package com.vk.enginegrip.editor.table

import com.intellij.database.datagrid.*
import com.intellij.database.run.ui.grid.GridMutationModel
import com.intellij.database.run.ui.grid.GridStorageAndModelUpdater
import com.intellij.database.run.ui.grid.editors.GridCellEditorHelper
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus
import com.vk.enginegrip.settings.EngineSettings
import kotlin.math.max
import kotlin.math.min

class EngineGridDataHookUp(project: Project, val messageBus: MessageBus) :
    GridDataHookUpBase<GridRow, GridColumn>(project) {
    private val myModel: DataGridListModel
    private val myMutationModel: GridMutationModel
    private val myModelUpdater: GridStorageAndModelUpdater
    private val myPageModel: MultiPageModelImpl<GridRow, GridColumn>
    private val myFilteringModel: EngineFilteringModel

    private val myLoader: EngineGridLoader

    init {
        myModel = DataGridListModel { v1: Any, v2: Any -> GridCellEditorHelper.areValuesEqual(v1, v2) }
        myMutationModel = GridMutationModel(this)
        myModelUpdater = GridStorageAndModelUpdater(myModel, myMutationModel, null)
        myPageModel = MultiPageModelImpl(myModel, EngineSettings.getSettings())

        myFilteringModel = EngineFilteringModel()

        myLoader = EngineGridLoader()
    }

    override fun getPageModel(): GridPagingModel<GridRow, GridColumn> = myPageModel

    override fun getLoader(): GridLoader = myLoader

    override fun getMutationModel(): GridModel<GridRow, GridColumn> = myMutationModel

    override fun getDataModel(): GridModel<GridRow, GridColumn> = myModel

    override fun isFilterApplicable(): Boolean = true

    override fun getFilteringModel(): GridFilteringModel = myFilteringModel

    override fun getFilterPrefix(): String = "PREFIX"

    private inner class EngineGridLoader : GridLoader {
        val columns: List<GridColumn> = listOf<GridColumn>(
            DataConsumer.Column(0, "Column Name", 1, null, null),
            DataConsumer.Column(1, "Column Value", 1, null, null),
        )

        private var totalRowCount = (100 * 3 + 50 + 6) // 356

        private var myRowsLoaded = -1

        override fun updateTotalRowCount(source: GridRequestSource) {
            println("!!! updateTotalRowCount")
            TODO("updateTotalRowCount")
        }

        override fun applyFilterAndSorting(source: GridRequestSource) {
            println("!!! applyFilterAndSorting")

            loadFirstPage(source)
        }

        override fun updateIsTotalRowCountUpdateable() {
            println("!!! updateIsTotalRowCountUpdateable")
            TODO("updateIsTotalRowCountUpdateable")
        }

        override fun reloadCurrentPage(source: GridRequestSource) {
            println("!!! reloadCurrentPage")

            val offset = max(0, myPageModel.pageStart - 1)
            load(source, offset)
        }

        // мы открыли страницу
        override fun loadFirstPage(source: GridRequestSource) {
            println("!!! loadFirstPage")

            load(source, 0)
        }

        // хотим открыть прошлую страницу
        override fun loadNextPage(source: GridRequestSource) {
            println("!!! loadNextPage")

            val offset = myPageModel.pageEnd
            load(source, offset)
        }

        // хотим открыть следующую страницу
        override fun loadPreviousPage(source: GridRequestSource) {
            println("!!! loadPreviousPage")

            val offset = max(0, myPageModel.pageStart - myPageModel.pageSize - 1)
            load(source, offset)
        }

        // хотим открыть последнюю страницу
        override fun loadLastPage(source: GridRequestSource) {
            println("!!! loadLastPage")

            val pageSize = myPageModel.pageSize

            val offset = -(if (pageSize > 0) pageSize else 100) - 1

            load(source, offset)
        }

        override fun load(source: GridRequestSource, offset: Int) {
            println("!!! load")
            println("offset: $offset")


            val pageSize = myPageModel.pageSize
            println("resultOffset: $offset, pageSize: $pageSize")

            doLoadDataFromDB(source, offset)
        }

        private fun getMockRows(offset: Int, pageSize: Int, filterPrefixText: String): List<GridRow> {

            val range = if (offset >= 0) {
                val offToPage = min(totalRowCount, offset + pageSize) - 1
                val rr = (offset..offToPage)
                rr
            } else {
                val rr = ((totalRowCount + offset + 1)..(totalRowCount - 1))
                rr
            }

            val rows: List<GridRow> = range.map { index ->
                var value: Int
                var stepCount = 100
                while (true) {
                    val randomValue = (0..100).random()
                    if (randomValue.toString().startsWith(filterPrefixText) && stepCount != 0) {
                        stepCount -= 1
                        continue
                    }

                    value = randomValue
                    break
                }

                DataConsumer.Row.create(index, arrayOf("-${index + 1}", "$value"))
            }

            return rows
        }


        private fun doLoadDataFromDB(source: GridRequestSource, offset: Int) {
            val pageSize = myPageModel.pageSize
            val filterText = myFilteringModel.filterText

            val rows = getMockRows(offset, pageSize, filterText)

            myPageModel.setTotalRowCount(totalRowCount.toLong(), true)

            val pageStart = rows[0].rowNum
            myPageModel.pageStart = pageStart

            myModelUpdater.removeRows(0, myModel.rowCount)
            myModelUpdater.setColumns(columns)
            myModelUpdater.addRows(rows)

            val pageEnd = rows[rows.size - 1].rowNum
            myPageModel.pageEnd = pageEnd

            myRowsLoaded += rows.size

            source.requestComplete(true)
        }
    }
}
