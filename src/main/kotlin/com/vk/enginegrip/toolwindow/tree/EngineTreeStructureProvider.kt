package com.vk.enginegrip.toolwindow.tree

import com.intellij.openapi.project.Project
import com.intellij.ui.tree.BaseTreeModel
import com.intellij.ui.treeStructure.SimpleNode
import com.intellij.util.concurrency.Invoker
import com.intellij.util.concurrency.InvokerSupplier
import com.vk.enginegrip.enigne.EngineActor
import com.vk.enginegrip.enigne.EngineConnectionDataStorage

class EngineTreeStructureProvider(val project: Project) : BaseTreeModel<EngineTreeNode>(), InvokerSupplier {
    private val invoker = Invoker.forBackgroundThreadWithReadAction(this)
    private var myRoot: EngineTreeNode

    init {
        myRoot = newRoot()
    }

    fun newRoot(): EngineTreeNode {
        val actors = EngineConnectionDataStorage.getInstance(project).getActors()
        val ch = actors.map {
            EngineTreeNode(project, it.name, connection = it.connection)
        }.toMutableList()

        val root = EngineTreeNode(project, "actors", ch)
        return root
    }

    override fun getRoot(): EngineTreeNode = myRoot

    override fun getChildren(parent: Any): List<EngineTreeNode> {
        val rootNode = parent as? EngineTreeNode ?: return emptyList()

        return rootNode.getChildren().toList()
    }

    fun refreshAll(actor: EngineActor) {
        val node = EngineTreeNode(project, actor.name, connection = actor.connection)

        myRoot.addChild(node)
        treeStructureChanged(null, null, null)
    }

    override fun getInvoker(): Invoker = invoker
}
