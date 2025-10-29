package com.vk.enginegrip.extractors

import com.intellij.application.options.CodeStyle
import com.intellij.database.datagrid.GridRow
import com.intellij.formatting.FormatTextRanges
import com.intellij.json.JsonLanguage
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.impl.source.codeStyle.CodeFormatterFacade
import com.intellij.testFramework.LightVirtualFile
import com.intellij.util.DocumentUtil
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class EngineFixturesDataConverter(val project: Project) {

    @Serializable
    private data class FixtureModel(
        val engine: String,
        val queries: List<Queries>
    )

    @Serializable
    private data class Queries(
        val method: String,
        val data: Map<String, String>
    )

    fun format(fileText: String): String? {
        val virtualFile = LightVirtualFile("Value Editor", JsonLanguage.INSTANCE, fileText)
        val document = FileDocumentManager.getInstance().getDocument(virtualFile) ?: return null

        val file = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: return null
        val codeFormatter = CodeFormatterFacade(CodeStyle.getSettings(file), virtualFile.language)
        val ranges = FormatTextRanges(TextRange.from(0, document.textLength), true)

        DocumentUtil.writeInRunUndoTransparentAction {
            codeFormatter.processText(file, ranges, false)
        }

        return file.text
    }

    fun convert(rows: List<GridRow>): String {
        val data: List<Queries> = rows.map { row ->
            val name = row.getValue(0) as String // TODO
            val value = row.getValue(1) as String // TODO

            Queries(
                method = "memcache.set",
                data = mapOf(
                    "key" to name,
                    "value" to value
                )
            )
        }

        val model = FixtureModel(
            engine = "confdata",
            queries = data
        )

        val text = Json.encodeToString(model)
        val formattedText = format(text) ?: text
        return formattedText
    }
}
