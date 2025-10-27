package com.vk.enginegrip.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.vk.enginegrip.dialog.EngineNewActorDialog

class EngineNewActorAction() : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        EngineNewActorDialog(e.project!!).show()
    }
}
