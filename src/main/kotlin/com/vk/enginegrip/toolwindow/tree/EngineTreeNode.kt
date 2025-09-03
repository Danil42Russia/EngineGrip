package com.vk.enginegrip.toolwindow.tree

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.SimpleNode

class EngineTreeNode(
    project: Project,
    private val name: String,
    private val children: Array<EngineTreeNode> = emptyArray(),
) : SimpleNode(project) {
    init {
//        presentation.setIcon(AllIcons.Gutter.WriteAccess)
    }

    override fun getName() = name

    override fun getChildren(): Array<EngineTreeNode> = children
}
