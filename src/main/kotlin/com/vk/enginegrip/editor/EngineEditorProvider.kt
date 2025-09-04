package com.vk.enginegrip.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.WeighedFileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.vk.enginegrip.editor.table.EngineTableFileEditor

class EngineEditorProvider : WeighedFileEditorProvider() {
    companion object {
        val ENGINE_KEY: Key<String> = Key("ENGINE_KEY")
    }

    override fun accept(
        project: Project,
        file: VirtualFile
    ): Boolean {
        return file.getUserData(ENGINE_KEY) != null
    }

    override fun createEditor(
        project: Project,
        file: VirtualFile
    ): FileEditor {
        return EngineTableFileEditor(project, file)
    }

    override fun getEditorTypeId(): String = "engine-editor"

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}
