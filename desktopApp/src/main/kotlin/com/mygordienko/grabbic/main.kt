package com.mygordienko.grabbic

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mygordienko.grabbic.clip.adapter.db.getClipSourceDatabase

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Grabbic",
    ) {
        App(
            clipSourceDatabase = getClipSourceDatabase(getDatabaseBuilder()),
            clipboardOperationService = DesktopClipboardOperationService()
        )
    }
}