package com.vk.enginegrip.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.WeighedFileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.vk.enginegrip.editor.table.EngineTableFileEditor
import com.vk.enginegrip.enigne.EngineActorConnection

class EngineEditorProvider : WeighedFileEditorProvider() {
    companion object {
        val ENGINE_KEY: Key<String> = Key("ENGINE_KEY")
        val ENGINE_CONNECTION: Key<EngineActorConnection> = Key("ENGINE_CONNECTION")

        val EDITOR_TYPE_ID = "engine-editor"
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file.getUserData(ENGINE_KEY) != null
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return EngineTableFileEditor(project, file)
    }

    override fun getEditorTypeId(): String = EDITOR_TYPE_ID

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}
