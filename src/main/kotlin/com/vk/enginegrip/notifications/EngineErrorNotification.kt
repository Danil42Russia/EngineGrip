package com.vk.enginegrip.notifications

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project

class EngineErrorNotification(
    content: String = "",
) : Notification("Engine Notification", "EngineGrip", content, NotificationType.ERROR) {

    fun show(project: Project? = null) {
        invokeLater {
            Notifications.Bus.notify(this, project)
        }
    }
}
