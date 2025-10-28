package com.vk.enginegrip.editor.tab

import com.intellij.database.datagrid.DataGrid
import com.intellij.database.datagrid.GridColumn
import com.intellij.database.datagrid.GridRow
import com.intellij.database.datagrid.ModelIndex
import com.intellij.database.run.ui.CellViewer
import com.intellij.database.run.ui.DataAccessType
import com.intellij.database.run.ui.UpdateEvent
import com.intellij.database.run.ui.grid.editors.GridCellEditorFactory.DefaultValueToText
import com.intellij.database.run.ui.grid.editors.GridCellEditorFactoryProvider
import com.intellij.ide.highlighter.HighlighterFactory
import com.intellij.json.JsonLanguage
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.UiDataProvider
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.testFramework.LightVirtualFile
import io.github.haradakunihiko.php_json_deserializer.PhpToJson
import javax.swing.JComponent

class PhpSerializedCellViewer(private val project: Project, private val grid: DataGrid) : CellViewer {
    private val editor: EditorEx = createEditor()

    private val wrappedComponent = UiDataProvider.wrapComponent(editor.component) { sink ->
        sink[CommonDataKeys.EDITOR] = editor
    }

    override val component: JComponent
        get() = wrappedComponent

    override val preferedFocusComponent: JComponent
        get() = editor.contentComponent

    override fun update(event: UpdateEvent?) {
        if (event is UpdateEvent.ValueChanged) {
            editor.selectionModel.removeSelection()
            val columnIdx = grid.selectionModel.leadSelectionColumn
            val rowIdx = grid.selectionModel.leadSelectionRow
            updateText(event.value, rowIdx, columnIdx)
            return
        }

        editor.selectionModel.removeSelection()
        updateText()
    }

    override fun dispose() {
        if (!editor.isDisposed) {
            EditorFactory.getInstance().releaseEditor(editor)
        }
    }

    private fun createEditor(): EditorEx {
        val virtualFile = LightVirtualFile("Value Editor", JsonLanguage.INSTANCE, "")
        val document = FileDocumentManager.getInstance().getDocument(virtualFile)
            ?: EditorFactory.getInstance().createDocument("")

        val highlighter = HighlighterFactory.createHighlighter(project, virtualFile)

        val editor = EditorFactory.getInstance().createViewer(document, project) as EditorEx
        editor.highlighter = highlighter
        editor.scrollPane.border = null
        editor.setShowPlaceholderWhenFocused(true)
        editor.settings.isLineNumbersShown = false
        return editor
    }

    private fun updateText() {
        val rowIdx = grid.selectionModel.leadSelectionRow
        val columnIdx = grid.selectionModel.leadSelectionColumn
        if (!rowIdx.isValid(grid) || !columnIdx.isValid(grid)) {
            return
        }
        val model = grid.getDataModel(DataAccessType.DATA_WITH_MUTATIONS)
        val value = model.getValueAt(rowIdx, columnIdx)

        updateText(value, rowIdx, columnIdx)
    }

    private fun updateText(value: Any?, rowIdx: ModelIndex<GridRow>, columnIdx: ModelIndex<GridColumn>) {
        val normalizeValue = if (value is String) {
            convertToJsonView(value)
        } else {
            value
        }

        val document = editor.document
        runWriteAction {
            val formatter = GridCellEditorFactoryProvider.get(grid)
                ?.getEditorFactory(grid, rowIdx, columnIdx)
                ?.getValueFormatter(grid, rowIdx, columnIdx, normalizeValue)
                ?: DefaultValueToText(grid, columnIdx, normalizeValue)

            val result = formatter.format()
            val file = FileDocumentManager.getInstance().getFile(document)
            file?.charset = result.charset
            file?.bom = result.bom

            println("!!! ${result.text}")
            document.setText(result.text)
        }
    }

    private fun convertToJsonView(value: String): String {
        // JSON: {"key": "value"}
        if (value.startsWith("{") && value.endsWith("}")) {
            return value
        }

        // JSON: []
        if (value.startsWith("[") && value.endsWith("]")) {
            return value
        }

        return try {
            PhpToJson.convert(value, prettyPrint = true)
        } catch (_: Exception) {
            ""
        }
    }
}
