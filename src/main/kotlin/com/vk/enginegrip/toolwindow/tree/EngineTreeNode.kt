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

    fun addChildNode(node: EngineTreeNode) {
        children.add(node)
    }

    fun removeChildNode(node: EngineTreeNode) {
        // TODO: после разделение такой хак больше будет не нужен
        val findNode = children.firstOrNull { it.name == node.name && it.connection == node.connection }

        if (children.contains(findNode)) {
            children.remove(findNode)
        } else {
            println("!!! ошибка в логике удаления элемента из дерева: $findNode")
        }
    }

    fun getConnection(): EngineActorConnection? = connection
}
