package com.mygordienko.grabbic

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mygordienko.grabbic.clip.adapter.ui.ClipSourceSelectionScreen
import com.mygordienko.grabbic.clip.adapter.db.ClipSourceDatabase
import com.mygordienko.grabbic.clip.adapter.db.RoomClipSourceRepository
import com.mygordienko.grabbic.clip.adapter.ui.AddClipSourceScreen
import com.mygordienko.grabbic.ui.theme.GrabbicTheme

@Composable
fun App(clipSourceDatabase: ClipSourceDatabase) {
    val repository = RoomClipSourceRepository(clipSourceDatabase)

    GrabbicTheme {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "clip-source-selection") {
            composable(route = "clip-source-selection") {
                ClipSourceSelectionScreen(repository = repository, navController)
            }

            composable(route = "add-clip-source") {
                AddClipSourceScreen(repository, navController)
            }
        }
    }
}