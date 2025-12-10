package com.vk.enginegrip.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.testFramework.LightVirtualFile
import com.intellij.ui.treeStructure.Tree
import com.vk.enginegrip.editor.EngineEditorProvider
import com.vk.enginegrip.toolwindow.file.EngineFileType
import com.vk.enginegrip.util.EngineTreeUtil.getSelectedNode

class EngineOpenConnectionAction(private val tree: Tree) : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val selectedNode = tree.getSelectedNode() ?: return

        val connection = selectedNode.getConnection() ?: return

        val tempFile = LightVirtualFile(selectedNode.name, EngineFileType(), "")
        tempFile.putUserData(
            EngineEditorProvider.ENGINE_KEY,
            ""
        )

        tempFile.putUserData(
            EngineEditorProvider.ENGINE_CONNECTION,
            connection
        )

        val fileEditor = FileEditorManager.getInstance(e.project!!)

        val openEngineEditors = fileEditor.allEditors.filter {
            val virtualFile = it.file
            virtualFile.fileType is EngineFileType && virtualFile.name == selectedNode.name
        }

        when (openEngineEditors.size) {
            0 -> fileEditor.openFile(tempFile, true)
            1 -> {
                // на самом деле это ошибка кода, надо избьавиться от LightVirtualFile,
                // тогда можно делать просто openFile и IDE сама поменяет фокус
                val openEngineEditor = openEngineEditors.first()
                fileEditor.openFile(openEngineEditor.file, true)
            }

            else -> {
                // хз как такое может получиться... но на всякий откроем
                fileEditor.openFile(tempFile, true)
            }
        }
    }
}
