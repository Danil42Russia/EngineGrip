package com.vk.enginegrip.toolwindow

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.vk.enginegrip.toolwindow.projectview.EngineViewPane
import java.awt.GridLayout
import javax.swing.JComponent
import javax.swing.JPanel

@Service(Service.Level.PROJECT)
class EngineToolWindowController(private val project: Project) : Disposable {
    companion object {
        fun getInstance(project: Project) = project.service<EngineToolWindowController>()
    }

    val projectPanel = EngineViewPane(project)

    private fun createToolbarPanel(targetComponent: JComponent): ActionToolbar {
//        val toolbarGroup = DefaultActionGroup()
        val toolbarGroup = ActionManager.getInstance().getAction("FindUsagesMenuGroup") as ActionGroup as ActionGroup

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
            "Temp",
            true
        ).let {
            contentManager.addContent(it)
        }
    }

    override fun dispose() {
        // TODO
    }
}
