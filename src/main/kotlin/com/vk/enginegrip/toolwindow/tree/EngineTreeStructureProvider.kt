package com.vk.enginegrip.toolwindow.tree

import com.intellij.openapi.project.Project
import com.intellij.ui.tree.BaseTreeModel
import com.intellij.util.concurrency.Invoker
import com.intellij.util.concurrency.InvokerSupplier
import javax.swing.event.TreeModelListener

class EngineTreeStructureProvider(val project: Project) : BaseTreeModel<EngineTreeNode>(), InvokerSupplier {
    private val invoker = Invoker.forBackgroundThreadWithReadAction(this)
    private val myRoot: EngineTreeNode

    init {
        val ch = EngineActorsList.LISTS.map {
            EngineTreeNode(project, it.name, connection = it.connection)
        }.toTypedArray()

        myRoot = EngineTreeNode(project, "actors", ch)
    }

    override fun getRoot(): EngineTreeNode = myRoot

    override fun getChildren(parent: Any): List<EngineTreeNode> {
        val rootNode = parent as? EngineTreeNode ?: return emptyList()

        return rootNode.getChildren().toList()
    }

    override fun getInvoker(): Invoker = invoker
}
