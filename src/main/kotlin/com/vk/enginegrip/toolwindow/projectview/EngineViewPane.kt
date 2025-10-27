package com.vk.enginegrip.toolwindow.projectview

import com.intellij.icons.AllIcons
import com.intellij.ide.SelectInTarget
import com.intellij.ide.dnd.aware.DnDAwareTree
import com.intellij.ide.projectView.impl.AbstractProjectViewPane
import com.intellij.ide.projectView.impl.ProjectViewTree
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ActionCallback
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.tree.AsyncTreeModel
import com.intellij.ui.treeStructure.Tree
import com.vk.enginegrip.actions.EngineOpenConnectionAction
import com.vk.enginegrip.toolwindow.tree.EngineTreeStructureProvider
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseEvent.BUTTON1
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeModel

// TODO: подумать как избавиться от AbstractProjectViewPane и стоит ли?
class EngineViewPane(project: Project, val model: TreeModel) : AbstractProjectViewPane(project) {

    fun init() {
        myTree = ProjectViewTree(model)
    }

    override fun getTitle(): String = "TODO 1"

    override fun getIcon(): Icon = AllIcons.Toolwindows.ToolWindowDataView

    override fun getId(): String = "TODO 2"

    override fun createComponent(): JComponent {
        val openConnectionAction = EngineOpenConnectionAction(myTree)
        myTree.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                if (e?.clickCount == 2 && e.button == BUTTON1) {
                    ActionManager.getInstance().tryToExecute(openConnectionAction, e, myTree, "", true)
                }
            }
        })

        val myScrollPane = ScrollPaneFactory.createScrollPane(myTree, true)
        return myScrollPane
    }

    override fun updateFromRoot(restoreExpandedPaths: Boolean): ActionCallback {
        TODO("updateFromRoot")
    }

    override fun select(
        element: Any?,
        file: VirtualFile?,
        requestFocus: Boolean
    ) {
        TODO("select")
    }

    override fun getWeight(): Int = 1

    override fun createSelectInTarget(): SelectInTarget {
        TODO("createSelectInTarget")
    }
}
