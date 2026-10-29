package com.mygordienko.grabbic.clip.adapter.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mygordienko.grabbic.ActiveClipSource
import com.mygordienko.grabbic.AddClipSource
import com.mygordienko.grabbic.clip.ClipSourceRepository
import com.mygordienko.grabbic.clip.ClipSourceSelectionScreenState
import com.mygordienko.grabbic.clip.ClipSourceSelectionViewModel
import com.mygordienko.grabbic.clip.domain.ClipSource
import grabbic.shared.generated.resources.Res
import grabbic.shared.generated.resources.clip_loading
import grabbic.shared.generated.resources.clip_no_sources
import grabbic.shared.generated.resources.clip_source_list_title
import grabbic.shared.generated.resources.icon_arrow_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun ClipSourceSelectionScreen(
    repository: ClipSourceRepository, navController: NavController) {
    val viewModel = viewModel { ClipSourceSelectionViewModel(repository) }
    val state = viewModel.state.collectAsStateWithLifecycle()

    fun navigateToAddNewSource() {
        navController.navigate(AddClipSource)
    }

    fun navigateBackToHome() {
        navController.popBackStack()
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
        ParentContainer(
            paddingValues = it,
            state = state.value,
            onBackClick = {
                navigateBackToHome()
            },
            onSourceClick = { it ->
                navController.navigate(ActiveClipSource(it.id))
            },
            onSourceDeleteClick = {
                viewModel.deleteSource(it)
            }
        )
    }
}


@Composable
fun ParentContainer(
    paddingValues: PaddingValues,
    state: ClipSourceSelectionScreenState,
    onBackClick: () -> Unit,
    onSourceClick: (ClipSource) -> Unit,
    onSourceDeleteClick: (ClipSource) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.icon_arrow_back),
                contentDescription = "back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = {
                        onBackClick()
                    })
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = stringResource(Res.string.clip_source_list_title),
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        if (state.isLoading) {
            LoadingView()
        } else if (state.sources.isEmpty()) {
            EmptyView()
        } else {
            ListSourcesScreen(
                state.sources,
                onClick = { it ->
                    onSourceClick(it)
                },
                onDelete = {
                    onSourceDeleteClick(it)
                }
            )
        }
    }
}

@Preview
@Composable
fun ParentContainerPreview() {
    ParentContainer(
        paddingValues = PaddingValues(8.dp),
        state = ClipSourceSelectionScreenState(
            isLoading = false,
            sources = emptyList()
        ),
        onBackClick = {},
        onSourceClick = {},
        onSourceDeleteClick = {}
    )
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