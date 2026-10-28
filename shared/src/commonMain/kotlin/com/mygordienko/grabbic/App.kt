package com.mygordienko.grabbic

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mygordienko.grabbic.clip.ClipSourceClient
import com.mygordienko.grabbic.clip.ClipboardOperationService
import com.mygordienko.grabbic.clip.adapter.db.ClipSourceDatabase
import com.mygordienko.grabbic.clip.adapter.db.RoomClipSourceRepository
import com.mygordienko.grabbic.clip.adapter.ui.ActiveClipSourceScreen
import com.mygordienko.grabbic.clip.adapter.ui.AddClipSourceScreen
import com.mygordienko.grabbic.clip.adapter.ui.ClipSourceSelectionScreen
import com.mygordienko.grabbic.ui.theme.GrabbicTheme
import kotlinx.serialization.Serializable

@Composable
fun App(
    clipSourceDatabase: ClipSourceDatabase,
    clipboardOperationService: ClipboardOperationService
) {
    val repository = RoomClipSourceRepository(clipSourceDatabase)
    val clipSourceClient = ClipSourceClient()

    GrabbicTheme {
        val navController = rememberNavController()

        NavHost(navController, startDestination = ClipSourceSelection) {
            composable<ClipSourceSelection> {
                ClipSourceSelectionScreen(repository = repository, navController)
            }

            composable<AddClipSource> {
                AddClipSourceScreen(repository, navController)
            }

            composable<ActiveClipSource> { backStackEntry ->
                val route = backStackEntry.toRoute<ActiveClipSource>()
                ActiveClipSourceScreen(
                    clipSourceId = route.clipSourceId,
                    clipSourceRepository = repository,
                    clipSourceClient = clipSourceClient,
                    clipboardOperationService = clipboardOperationService,
                    onMissingClipSource = {
                        navController.navigate(ClipSourceSelection)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}


@Serializable
object ClipSourceSelection

@Serializable
object AddClipSource


@Serializable
data class ActiveClipSource(
    val clipSourceId: String
)