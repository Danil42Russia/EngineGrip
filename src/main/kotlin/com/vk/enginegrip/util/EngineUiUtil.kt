package com.vk.enginegrip.util

import com.intellij.database.datagrid.DataGrid
import com.intellij.util.ui.UIUtil

object EngineUiUtil {
    fun setProgressMessage(grid: DataGrid, message: String?) {
        UIUtil.invokeLaterIfNeeded({
            val loadingPanel = grid.panel.component
            if (message != null) {
                loadingPanel.startLoading()
            } else {
                loadingPanel.stopLoading()
            }
            loadingPanel.setLoadingText(message)
        })
    }
}
