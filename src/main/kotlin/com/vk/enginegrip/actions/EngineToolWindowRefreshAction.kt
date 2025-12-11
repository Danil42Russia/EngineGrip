package com.vk.enginegrip.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.tree.AsyncTreeModel
import com.intellij.ui.treeStructure.Tree

class EngineToolWindowRefreshAction(
    private val tree: Tree,
    private val model: AsyncTreeModel,
) : AnAction("Reload", null, AllIcons.Actions.Refresh) {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        tree.model = model
        tree.updateUI()
    }
}
