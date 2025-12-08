package com.vk.enginegrip.toolwindow

import com.intellij.ide.projectView.impl.ProjectViewTree
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.tree.AsyncTreeModel
import com.vk.enginegrip.bus.EngineActorTopics
import com.vk.enginegrip.enigne.EngineActor
import com.vk.enginegrip.toolwindow.projectview.EngineViewPane
import com.vk.enginegrip.toolwindow.tree.EngineTreeStructureProvider
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JPanel

// https://github.com/JetBrains/intellij-plugins/blob/62f089424db5a4a6a6b7bf681c5831ab6ed4fc3d/makefile/src/com/jetbrains/lang/makefile/toolWindow/MakeToolWindowFactory.kt#L88

@Service(Service.Level.PROJECT)
class EngineToolWindowController(private val project: Project) : Disposable {
    companion object {
        fun getInstance(project: Project) = project.service<EngineToolWindowController>()
    }

    val treePanel = EngineTreeStructureProvider(project)
    val asyncTreeModel = AsyncTreeModel(treePanel, this)
    val projectPanel = EngineViewPane(project, asyncTreeModel)

    init {
        project.messageBus.connect().subscribe(EngineActorTopics.TOPIC, object : EngineActorTopics {
            override fun onNewActor(actor: EngineActor) {
                refreshAll(actor)
            }
        })
    }


    private fun createToolbarPanel(targetComponent: JComponent): ActionToolbar {
        val toolbarGroup = ActionManager.getInstance().getAction("ActionMenuGroup") as ActionGroup

        val toolbar = ActionManager.getInstance()
            .createActionToolbar("EngineToolWindowToolbar", toolbarGroup, true)
        toolbar.targetComponent = targetComponent
        return toolbar
    }

    fun setUp(toolWindow: ToolWindow) {
        projectPanel.init()
        val contentManager = toolWindow.contentManager
        val projectPanelComponent = projectPanel.createComponent()

        val treePanel = SimpleToolWindowPanel(true).apply {
            setContent(projectPanelComponent)
        }

        val toolBarPanel = JPanel(GridLayout())
        val toolbar = createToolbarPanel(projectPanelComponent)
        toolBarPanel.add(toolbar.component)

        treePanel.toolbar = toolBarPanel

        contentManager.factory.createContent(
            treePanel,
            null,
            true
        ).let {
            contentManager.addContent(it)
        }
    }

    fun refreshAll(actor: EngineActor) {
        treePanel.refreshAll(actor)
    }

    override fun dispose() {
        // TODO
    }
}
