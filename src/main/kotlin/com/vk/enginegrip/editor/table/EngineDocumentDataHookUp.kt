package com.vk.enginegrip.editor.table

import com.intellij.database.datagrid.DocumentDataHookUp
import com.intellij.database.datagrid.GridLoader
import com.intellij.database.datagrid.GridRequestSource
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project

class EngineDocumentDataHookUp(project: Project, document: Document) :
    DocumentDataHookUp(project, document, null) {
    override fun buildMarkup(
        sequence: CharSequence,
        myRequestSource: GridRequestSource
    ): DataMarkup {
        println("!!!! buildMarkup")
        throw RuntimeException()
    }

    override fun getLoader(): GridLoader = EngineLoader()
}
