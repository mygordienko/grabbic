package com.mygordienko.grabbic

import androidx.compose.runtime.*
import com.mygordienko.grabbic.clip.adapter.ui.ClipSourceSelectionScreen
import com.mygordienko.grabbic.clip.adapter.db.ClipSourceDatabase
import com.mygordienko.grabbic.clip.adapter.db.RoomClipSourceRepository
import com.mygordienko.grabbic.ui.theme.GrabbicTheme

@Composable
fun App(clipSourceDatabase: ClipSourceDatabase) {
    GrabbicTheme {
        ClipSourceSelectionScreen(repository = RoomClipSourceRepository(clipSourceDatabase))
    }
}