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
        val ch = (1..100).map {
            val tn = EngineTreeNode(project, it.toString())
//            tn.update()

            tn
        }.toTypedArray()

        myRoot = EngineTreeNode(project, "root", ch)
    }

    override fun getRoot(): EngineTreeNode = myRoot

    override fun getChildren(parent: Any): List<EngineTreeNode> {
        val rootNode = parent as? EngineTreeNode ?: return emptyList()

        return rootNode.getChildren().toList()
    }

    override fun getInvoker(): Invoker = invoker
}
