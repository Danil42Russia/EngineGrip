package com.vk.enginegrip.toolwindow.projectview

import com.intellij.icons.AllIcons
import com.intellij.ide.SelectInTarget
import com.intellij.ide.projectView.impl.AbstractProjectViewPane
import com.intellij.ide.projectView.impl.ProjectViewTree
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.actionSystem.DataSink
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ActionCallback
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.ScrollPaneFactory
import com.vk.enginegrip.actions.EngineOpenConnectionAction
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseEvent.BUTTON1
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.tree.TreeModel

// TODO: подумать как избавиться от AbstractProjectViewPane и стоит ли?
class EngineViewPane(project: Project, val model: TreeModel) : AbstractProjectViewPane(project) {

    companion object {
        @JvmField
        val SELECTED_NODES: DataKey<String> = DataKey.create("selectedNodes")
    }

    fun init() {
        myTree = ProjectViewTree(model)
    }

    override fun getTitle(): String = "TODO 1"

    override fun getIcon(): Icon = AllIcons.Toolwindows.ToolWindowDataView

    override fun getId(): String = "TODO 2"

    override fun uiDataSnapshot(sink: DataSink) {
        sink[SELECTED_NODES] = "selectedNodes"
        super.uiDataSnapshot(sink)
    }

    override fun uiDataSnapshotForSelection(
        sink: DataSink,
        selectedUserObjects: Array<out Any?>,
        singleSelectedPathUserObjects: Array<out Any?>?
    ) {
        super.uiDataSnapshotForSelection(sink, selectedUserObjects, singleSelectedPathUserObjects)
    }

    override fun createComponent(): JComponent {

//        https://github.com/JetBrains/intellij-community/blob/c2d8ce5e51a09d6d9208fad3572f430c033a6c7a/platform/xdebugger-impl/src/com/intellij/xdebugger/impl/ui/tree/XDebuggerTree.java#L393
//        myTree.addTreeSelectionListener { event ->
//            val node = myTree.getSelectedNodes(EngineTreeNode::class.java, null).firstOrNull()
//            SELECTED_NODES.
//        }

//        (object : TreeSelectionListener {
//            override fun valueChanged(e: TreeSelectionEvent?) {
//                println("!!! tofo2")
//            }
//
//        })

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
