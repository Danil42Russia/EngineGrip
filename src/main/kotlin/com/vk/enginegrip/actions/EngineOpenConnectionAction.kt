package com.vk.enginegrip.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.testFramework.LightVirtualFile
import com.intellij.ui.treeStructure.Tree
import com.vk.enginegrip.editor.EngineEditorProvider
import com.vk.enginegrip.toolwindow.file.EngineFileType
import com.vk.enginegrip.toolwindow.tree.EngineTreeNode

class EngineOpenConnectionAction(private val tree: Tree) : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val selectedNode = tree.getSelectedNodes(EngineTreeNode::class.java) { true }.firstOrNull()
        if (selectedNode == null) {
            return
        }

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

        FileEditorManager.getInstance(e.project!!).openFile(tempFile, true)
    }
}
