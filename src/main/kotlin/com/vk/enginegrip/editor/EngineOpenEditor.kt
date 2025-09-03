package com.vk.enginegrip.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBColor
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JTextField
import javax.swing.JPanel

class EngineOpenEditor(private val project: Project, private val file: VirtualFile) : UserDataHolderBase(), FileEditor {

    private val mainComponent: JComponent = JPanel()

    init {
        val label = JLabel("Engine Open Editor").apply {
            foreground = JBColor.RED
        }

        mainComponent.add(label)
    }


    override fun getComponent(): JComponent {
        return mainComponent
    }

    override fun getPreferredFocusedComponent(): JComponent {
        return mainComponent
    }

    override fun getName(): String {
        return "TODO"
    }

    override fun setState(state: FileEditorState) {
    }

    override fun isModified(): Boolean = false

    override fun isValid(): Boolean = true

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun getFile(): VirtualFile = file

    override fun dispose() {
    }
}
