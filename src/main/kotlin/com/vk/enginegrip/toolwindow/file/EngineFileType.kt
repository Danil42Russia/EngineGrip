package com.vk.enginegrip.toolwindow.file

import com.intellij.icons.AllIcons
import com.intellij.openapi.fileTypes.FileType
import javax.swing.Icon

class EngineFileType : FileType {
    override fun getName(): String = ""

    override fun getDescription(): String = ""

    override fun getDefaultExtension(): String = ""

    override fun getIcon(): Icon = AllIcons.Actions.ProfileRed

    override fun isBinary(): Boolean = true
}
