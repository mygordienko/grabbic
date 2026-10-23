package com.mygordienko.grabbic.clip.adapter.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mygordienko.grabbic.clip.ClipSourceRepository
import com.mygordienko.grabbic.clip.ClipSourceSelectionViewModel
import grabbic.shared.generated.resources.Res
import grabbic.shared.generated.resources.clip_loading
import grabbic.shared.generated.resources.clip_no_sources
import grabbic.shared.generated.resources.clip_source_list_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun ClipSourceSelectionScreen(repository: ClipSourceRepository, navController: NavController) {
    val viewModel = viewModel { ClipSourceSelectionViewModel(repository) }
    val state = viewModel.state.collectAsStateWithLifecycle()

    fun navigateToAddNewSource() {
        navController.navigate("add-clip-source")
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primary),
                onClick = {
                    navigateToAddNewSource()
                }
            ){
                Text("+")
            }
        }
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(
                text = stringResource(Res.string.clip_source_list_title),
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            if (state.value.isLoading) {
                LoadingView()
            } else if (state.value.sources.isEmpty()) {
                EmptyView()
            } else {
                ListSourcesScreen(
                    state.value.sources,
                    onDelete = {
                        viewModel.deleteSource(it)
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyView() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painterResource(Res.drawable.clip_no_sources),
                contentDescription = "image",
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = stringResource(Res.string.clip_no_sources),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            CircularProgressIndicator()
            Text(
                text = stringResource(Res.string.clip_loading)
            )
        }
    }
}