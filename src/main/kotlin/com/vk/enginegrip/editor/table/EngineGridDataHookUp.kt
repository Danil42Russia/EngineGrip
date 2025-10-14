package com.vk.enginegrip.editor.table

import com.intellij.database.datagrid.*
import com.intellij.database.run.ui.grid.GridMutationModel
import com.intellij.database.run.ui.grid.GridStorageAndModelUpdater
import com.intellij.database.run.ui.grid.editors.GridCellEditorHelper
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus
import com.vk.enginegrip.bus.EngineBusTopics
import com.vk.enginegrip.http.EngineJRPClient
import com.vk.enginegrip.http.WildcardPaginationParams
import com.vk.enginegrip.settings.EngineSettings
import com.vk.enginegrip.task.BackgroundTask
import java.awt.EventQueue
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

    //    private val jrpClient = EngineJRPClient("http://localhost:8100", 100);
    private val jrpClient = EngineJRPClient("https://danilovchinnikov.dev.vk-apps.com", 14600);

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
            DataConsumer.Column(0, "Key Name", 1, null, null),
            DataConsumer.Column(1, "Key Origin", 1, null, null),
            DataConsumer.Column(2, "Key Value", 1, null, null),
        )

        //        private var totalRowCount = 300 + 60 + 5
        private var totalRowCount = 0

        private var myRowsLoaded = -1

        private var pageStarkKeyLoaded = ""
        private var pageStarkRowsLoaded = -1

        private var pageEndKeyLoaded = ""

        private val concatKeyName = true

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

            pageEndKeyLoaded = pageStarkKeyLoaded
            myRowsLoaded = pageStarkRowsLoaded
            val offset = max(0, myPageModel.pageStart - 1)
            load(source, offset)
        }

        // мы открыли страницу
        override fun loadFirstPage(source: GridRequestSource) {
            println("!!! loadFirstPage")

            pageStarkKeyLoaded = ""
            pageEndKeyLoaded = ""
            myRowsLoaded = 0
            pageStarkRowsLoaded = 0
            totalRowCount = jrpClient.getWildcardCount(myFilteringModel.filterText)?.count ?: 0
            load(source, 0)
        }

        // хотим открыть следующую страницу
        override fun loadNextPage(source: GridRequestSource) {
            println("!!! loadNextPage")

            val offset = myPageModel.pageEnd
            load(source, offset)
        }

        // хотим открыть прошлую страницу
        override fun loadPreviousPage(source: GridRequestSource) {
            println("!!! loadPreviousPage")

            val offset = max(0, myPageModel.pageStart - myPageModel.pageSize - 1)
            // load(source, offset)
        }

        // хотим открыть последнюю страницу
        override fun loadLastPage(source: GridRequestSource) {
            println("!!! loadLastPage")

            val pageSize = myPageModel.pageSize

            val offset = -(if (pageSize > 0) pageSize else 100) - 1

            // load(source, offset)
        }

        override fun load(source: GridRequestSource, offset: Int) {
            println("!!! load")
            println("offset: $offset")


            val pageSize = myPageModel.pageSize
            println("resultOffset: $offset, pageSize: $pageSize")

//            doLoadDataFromDB(source, offset)
            doLoadData(source, offset)
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

                val keyName = "-${index + 1}"
                val keyOrigin = value.toString()

//                val kekValue = """{"$keyOrigin": $keyName}"""

                val kekValue = """a:1:{s:${keyOrigin.length}:"$keyOrigin";s:${keyName.length}:"$keyName";}"""

                DataConsumer.Row.create(index, arrayOf(keyName, keyOrigin, kekValue))
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

        private fun doLoadData(source: GridRequestSource, offset: Int) {
            val progressListener = messageBus.syncPublisher(EngineBusTopics.PROGRESS_TOPIC)
            val pageSize = myPageModel.pageSize
            val filterText = myFilteringModel.filterText

            BackgroundTask.runTask(project, "Querying request") {
                progressListener.taskStarted()
                myPageModel.setTotalRowCount(totalRowCount.toLong(), true)

                progressListener.sendingRequest()

                val limit = WildcardPaginationParams(pageSize, pageEndKeyLoaded)
                val response = jrpClient.getWildcardDictWithPagination(filterText, limit)
                if (response == null) {
                    progressListener.taskFinished()
                    source.requestComplete(false)
                    return@runTask
                }

                progressListener.processingRequest()
                var index = myRowsLoaded
                val rows = mutableListOf<GridRow>()
                response.result.forEach { (key, keyResult) ->
                    val keyName = if (concatKeyName) "$filterText$key" else key

                    val keyValue = keyResult.value

                    val value = arrayOf(key, keyName, keyValue)
                    val row = DataConsumer.Row.create(index, value)
                    rows.add(row)
                    index++
                }

                // TODO
                if (rows.isEmpty()) {
                    myModelUpdater.removeRows(0, myModel.rowCount)
                    progressListener.taskFinished()
                    source.requestComplete(true)
                    return@runTask
                }

                val pageStart = rows[0]
                myPageModel.pageStart = pageStart.rowNum

                pageStarkKeyLoaded = pageStart.getValue(0).toString()
                pageStarkRowsLoaded = pageStart.rowNum

                EventQueue.invokeLater {
                    myModelUpdater.removeRows(0, myModel.rowCount)
                    myModelUpdater.setColumns(columns)
                    myModelUpdater.addRows(rows)
                }

                val pageEnd = rows[rows.size - 1]
                myPageModel.pageEnd = pageEnd.rowNum

                pageEndKeyLoaded = pageEnd.getValue(0).toString()
                myRowsLoaded += rows.size

                progressListener.taskFinished()
                source.requestComplete(true)
            }
        }
    }
}
