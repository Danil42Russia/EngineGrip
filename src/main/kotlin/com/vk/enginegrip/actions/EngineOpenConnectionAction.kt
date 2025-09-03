package com.vk.enginegrip.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.testFramework.LightVirtualFile
import com.vk.enginegrip.editor.EngineEditorProvider
import com.vk.enginegrip.toolwindow.file.EngineFileType

class EngineOpenConnectionAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val tempFile = LightVirtualFile("Test", EngineFileType(), "")
        tempFile.putUserData(
            EngineEditorProvider.ENGINE_KEY,
            ""
        )

        FileEditorManager.getInstance(e.project!!).openFile(tempFile, true)
    }
}
