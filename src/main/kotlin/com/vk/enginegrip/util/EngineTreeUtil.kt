package com.vk.enginegrip.util

import com.intellij.ui.treeStructure.Tree
import com.vk.enginegrip.toolwindow.tree.EngineTreeNode

object EngineTreeUtil {
    fun Tree.getSelectedNode(): EngineTreeNode? {
        val selectedNodes = this.getSelectedNodes(EngineTreeNode::class.java, null)
        return selectedNodes.firstOrNull()
    }
}
