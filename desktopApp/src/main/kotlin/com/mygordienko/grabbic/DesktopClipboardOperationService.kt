package com.mygordienko.grabbic

import co.touchlab.kermit.Logger
import com.mygordienko.grabbic.clip.ClipboardOperationService
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

class DesktopClipboardOperationService: ClipboardOperationService {
    override suspend fun read(): String {
        try {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                return clipboard.getData(DataFlavor.stringFlavor) as String
            } else {
                Logger.i { "Not a text clipboard" }
                return ""
            }
        } catch (e: Exception) {
            Logger.e { "Error reading clipboard" }
            return ""
        }
    }

    override suspend fun clear() {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection("")
        clipboard.setContents(selection, selection)
    }

    override suspend fun write(data: String) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val selection = StringSelection(data)
        clipboard.setContents(selection, selection)
    }
}