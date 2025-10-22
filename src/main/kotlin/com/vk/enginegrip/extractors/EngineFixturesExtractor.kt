package com.vk.enginegrip.extractors

import com.intellij.database.datagrid.GridColumn
import com.intellij.database.datagrid.GridRow
import com.intellij.database.extractors.DataExtractor
import com.intellij.database.extractors.DefaultValuesExtractor
import com.intellij.database.extractors.ExtractionConfig
import com.intellij.database.extractors.ObjectFormatter
import com.intellij.database.util.Out

class EngineFixturesExtractor(converter: ObjectFormatter) : DefaultValuesExtractor(converter) {
    override fun getFileExtension(): String {
        return "json"
    }

    override fun supportsText(): Boolean {
        return true // ??
    }

    override fun isStringLiteral(
        row: GridRow?,
        column: GridColumn?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun startExtraction(
        out: Out,
        allColumns: List<GridColumn>,
        query: String,
        config: ExtractionConfig,
        vararg selectedColumns: Int
    ): DataExtractor.Extraction {
        return SqlExtractionBase(out, config, allColumns, query, selectedColumns)
    }

    private class SqlExtractionBase(
        private val out: Out,
        config: ExtractionConfig,
        allColumns: List<GridColumn>,
        query: String,
        selectedColumnIndices: IntArray,
    ) : DefaultExtraction(out, config, allColumns, query, selectedColumnIndices) {
        override fun appendData(rows: List<GridRow>) {

            out.appendText("""{"todo": "new"}""")
        }
    }
}