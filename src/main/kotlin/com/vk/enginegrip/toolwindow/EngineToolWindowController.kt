package com.vk.enginegrip.toolwindow

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.TreeUIHelper
import com.intellij.ui.tree.AsyncTreeModel
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import com.vk.enginegrip.actions.EngineDeleteActorAction
import com.vk.enginegrip.actions.EngineEditActorAction
import com.vk.enginegrip.actions.EngineNewActorAction
import com.vk.enginegrip.actions.EngineOpenConnectionAction
import com.vk.enginegrip.bus.EngineActorTopics
import com.vk.enginegrip.enigne.EngineActor
import com.vk.enginegrip.toolwindow.tree.EngineTreeStructureProvider
import java.awt.GridLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseEvent.BUTTON1
import javax.swing.JPanel
import javax.swing.tree.TreeSelectionModel

@Service(Service.Level.PROJECT)
class EngineToolWindowController(private val project: Project) : Disposable {
    companion object {
        fun getInstance(project: Project) = project.service<EngineToolWindowController>()
    }

    val treePanel = EngineTreeStructureProvider(project)
    val asyncTreeModel = AsyncTreeModel(treePanel, this)

    init {
        project.messageBus.connect().subscribe(EngineActorTopics.TOPIC, object : EngineActorTopics {
            override fun onCreateActor(actor: EngineActor) {
                createNode(actor)
            }

            override fun onDeleteActor(actor: EngineActor) {
                deleteNode(actor)
            }
        })
    }

    private fun createToolbarPanel(tree: Tree): ActionToolbar {
        val group = DefaultActionGroup()

        group.add(EngineNewActorAction())
//        group.add(EngineEditActorAction())
        group.add(EngineDeleteActorAction(tree))

        val toolbar = ActionManager.getInstance()
            .createActionToolbar("EngineToolWindowToolbar", group, true)

        toolbar.targetComponent = tree
        return toolbar
    }

    fun setUp(toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val panel = SimpleToolWindowPanel(true)

        val tree = object : Tree(asyncTreeModel), UiDataProvider {
            override fun uiDataSnapshot(sink: DataSink) {
                // TODO: что-то сюда добавить
            }
        }.apply {
            selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
            isRootVisible = false
            showsRootHandles = true
        }

        TreeUtil.installActions(tree)
        TreeUIHelper.getInstance().installTreeSpeedSearch(tree)

        panel.add(ScrollPaneFactory.createScrollPane(tree))

        val openConnectionAction = EngineOpenConnectionAction(tree)
        tree.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                if (e?.clickCount == 2 && e.button == BUTTON1) {
                    ActionManager.getInstance().tryToExecute(
                        openConnectionAction,
                        e,
                        tree,
                        "",
                        true
                    )
                }
            }
        })

        val toolBarPanel = JPanel(GridLayout())
        val toolbar = createToolbarPanel(tree)
        toolBarPanel.add(toolbar.component)

        panel.toolbar = toolBarPanel

        contentManager.factory.createContent(
            panel,
            null,
            true
        ).let {
            contentManager.addContent(it)
        }
    }

    fun createNode(actor: EngineActor) {
        treePanel.createNode(actor)
        treePanel.refreshAll()
    }

    fun deleteNode(actor: EngineActor) {
        treePanel.deleteNode(actor)
        treePanel.refreshAll()
    }

    override fun dispose() {
        // TODO
    }
}
