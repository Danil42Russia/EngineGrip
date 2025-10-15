package com.vk.enginegrip.toolwindow.tree

import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.SimpleNode
import com.vk.enginegrip.enigne.EngineActorConnection

// TODO: разделить на root и children
class EngineTreeNode(
    project: Project,
    private val name: String,
    private val children: Array<EngineTreeNode> = emptyArray(),
    private val connection: EngineActorConnection? = null
) : SimpleNode(project) {
    init {
//        presentation.setIcon(AllIcons.Gutter.WriteAccess)

    }

    override fun getName() = name

    override fun getChildren(): Array<EngineTreeNode> = children

    fun getConnection(): EngineActorConnection? = connection
}
