package com.vk.enginegrip.editor.table

import com.intellij.database.datagrid.GridFilteringModel
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.util.EventDispatcher

class EngineFilteringModel : GridFilteringModel {
    private val myEventDispatcher = EventDispatcher.create(GridFilteringModel.Listener::class.java)
    private val myDocument = EditorFactory.getInstance().createDocument("")

    override fun getFilterText(): String {
        return myDocument.text
    }

    override fun getAppliedText(): String {
        TODO("getAppliedText")
    }

    override fun isIgnoreCurrentText(): Boolean {
        TODO("isIgnoreCurrentText")
    }

    override fun setIgnoreCurrentText(ignore: Boolean) {
        TODO("setIgnoreCurrentText")
    }

    override fun setFilterText(text: String) {
        TODO("setFilterText")
    }

    override fun setHistory(history: List<String?>) {
        TODO("setHistory")
    }

    override fun getHistory(): List<String?> {
        TODO("getHistory")
    }

    override fun getFilterDocument(): Document = myDocument

    override fun addListener(
        l: GridFilteringModel.Listener,
        disposable: Disposable
    ) {
        myEventDispatcher.addListener(l, disposable)
    }

    override fun applyCurrentText() {
        TODO("applyCurrentText")
    }
}
