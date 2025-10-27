package com.vk.enginegrip.toolwindow.tree

import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.SimpleNode
import com.vk.enginegrip.enigne.EngineActorConnection

// TODO: разделить на root и children
class EngineTreeNode(
    project: Project,
    private val name: String,
    private val children: MutableList<EngineTreeNode> = mutableListOf(),
    private val connection: EngineActorConnection? = null
) : SimpleNode(project) {
    init {
//        presentation.setIcon(AllIcons.Gutter.WriteAccess)

    }

    override fun getName() = name

    override fun getChildren(): Array<EngineTreeNode> = children.toTypedArray()

    fun addChild(node: EngineTreeNode) {
        children.add(node)
    }

    fun getConnection(): EngineActorConnection? = connection
}
