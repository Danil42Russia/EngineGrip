package com.vk.enginegrip.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.*
import com.vk.enginegrip.bus.EngineActorTopics
import com.vk.enginegrip.enigne.EngineActor
import com.vk.enginegrip.enigne.EngineActorConnection
import com.vk.enginegrip.enigne.EngineConnectionDataStorage
import javax.swing.JComponent
import javax.swing.JTextField

class EngineNewActorDialog(private val project: Project) : DialogWrapper(project, true) {
    var name: String = ""
        private set
    var actorID: Int = 0
        private set
    var jrpURL: String = ""
        private set

    init {
        title = "New Actor"
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        row("Name:") {
            textField()
                .bindText(::name)
                .align(Align.FILL)
//                .required()
        }

        separator()

        row("Actor ID:") {
            intTextField()
                .bindIntText(::actorID)
                .align(Align.FILL)
        }

        row("JRP URL:") {
            textField()
                .bindText(::jrpURL)
                .align(Align.FILL)
//                .required()
        }

        row("Engine:") {
            comboBox(listOf("pmemcached"))
                .align(Align.FILL)
        }

        separator()

        row {
            link(
                "Test connection",
                action = {}
            ).gap(RightGap.SMALL)

            comment("unknown JRP version")
                .gap(RightGap.SMALL)
        }
    }

    override fun doOKAction() {
        super.applyFields()

        val inner = EngineActor(
            name = name,
            connection = EngineActorConnection(
                url = jrpURL,
                actor = actorID,
            ),
        )

        val storage = EngineConnectionDataStorage.getInstance(project)
        storage.addInnerActor(inner)

        project.messageBus.syncPublisher(EngineActorTopics.TOPIC).onNewActor(inner)
        super.doOKAction()
    }

    private fun <T : JTextField> Cell<T>.required(): Cell<T> = this
        .errorOnApply("This field is required") { it.isEnabled && it.text.isEmpty() }
}
