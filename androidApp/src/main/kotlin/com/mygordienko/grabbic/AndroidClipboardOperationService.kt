package com.mygordienko.grabbic

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.mygordienko.grabbic.clip.ClipboardOperationService

class AndroidClipboardOperationService(private val context: Context): ClipboardOperationService {
    override suspend fun read(): String {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip

        if (clipData == null) {
            return ""
        }

        val sb = StringBuilder("")
        for (i in 0..<clipData.itemCount) {
            clipData.getItemAt(i)?.coerceToText(context)?.let {
                sb.append(it)
            }
        }

        return sb.toString()
    }

    override suspend fun clear() {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", "")
        clipboardManager.setPrimaryClip(clip)
    }

    override suspend fun write(data: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", data)
        clipboardManager.setPrimaryClip(clip)
    }
}