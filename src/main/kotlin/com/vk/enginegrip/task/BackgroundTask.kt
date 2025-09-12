package com.vk.enginegrip.task

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsContexts

object BackgroundTask {
    inline fun runTask(
        project: Project,
        @NlsContexts.ProgressTitle title: String,
        crossinline task: (indicator: ProgressIndicator) -> Unit
    ) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, title, true) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                task(indicator)
            }
        })
    }
}
