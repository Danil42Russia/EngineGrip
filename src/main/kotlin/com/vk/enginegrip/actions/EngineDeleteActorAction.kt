package com.vk.enginegrip.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.treeStructure.Tree
import com.vk.enginegrip.bus.EngineActorTopics
import com.vk.enginegrip.enigne.EngineActor
import com.vk.enginegrip.enigne.EngineConnectionDataStorage
import com.vk.enginegrip.util.EngineTreeUtil.getSelectedNode

class EngineDeleteActorAction(private val tree: Tree) :
    AnAction("Delete Connection", null, AllIcons.General.Delete) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!

        val selectedNode = tree.getSelectedNode()!!
        val connection = selectedNode.getConnection()!!

        val actor = EngineActor(
            selectedNode.name,
            connection
        )

        val storage = EngineConnectionDataStorage.getInstance(project)
        storage.removeInnerActor(actor)

        // WTF ?!
        project.messageBus.syncPublisher(EngineActorTopics.TOPIC).onDeleteActor(actor)
    }
}
