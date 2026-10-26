package com.mygordienko.grabbic.clip.adapter.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mygordienko.grabbic.clip.ActiveClipViewModel
import com.mygordienko.grabbic.clip.ClipSourceClient
import com.mygordienko.grabbic.clip.ClipSourceRepository
import com.mygordienko.grabbic.clip.ClipboardOperationService
import com.mygordienko.grabbic.clip.domain.ClipSource
import com.mygordienko.grabbic.common.toHexString
import grabbic.shared.generated.resources.Res
import grabbic.shared.generated.resources.clip_error_missing_source
import grabbic.shared.generated.resources.common_action_go_back
import grabbic.shared.generated.resources.error
import grabbic.shared.generated.resources.icon_back
import grabbic.shared.generated.resources.icon_clear
import grabbic.shared.generated.resources.icon_copy
import grabbic.shared.generated.resources.icon_get
import grabbic.shared.generated.resources.no_data
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun ActiveClipSourceScreen(
    clipSourceId: String,
    clipSourceRepository: ClipSourceRepository,
    clipSourceClient: ClipSourceClient,
    clipboardOperationService: ClipboardOperationService,
    onMissingClipSource: () -> Unit,
    onBackClick: () -> Unit
) {

    val viewModel = viewModel { ActiveClipViewModel(clipSourceRepository, clipSourceClient, clipboardOperationService) }
    val clipOperationState = viewModel.clipOperationState.collectAsStateWithLifecycle()

    var floatingButtonMenuExpanded by remember {mutableStateOf(false)}
    var clipSource = remember { mutableStateOf<ClipSource?>(null) }

    LaunchedEffect(true) {
        viewModel.loadSource(clipSourceId).collectLatest {
            if (it == null) {
                onMissingClipSource()
            } else {
                clipSource.value = it
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButtonMenu(
                expanded = floatingButtonMenuExpanded,
                button =  {
                    FloatingActionButton(
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primary),
                        onClick = { floatingButtonMenuExpanded = !floatingButtonMenuExpanded}
                    ){
                        Text("☰")
                    }
                },
                horizontalAlignment = Alignment.End,
                content = {
                    FloatingActionButtonMenuItem(
                        onClick = {
                            viewModel.clear()
                        },
                        text = { },
                        icon =  { Icon(
                            painter = painterResource(Res.drawable.icon_clear),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        ) },
                        containerColor = MaterialTheme.colorScheme.primaryFixedDim,
                        contentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primaryFixedDim),
                    )
                    FloatingActionButtonMenuItem(
                        onClick = {
                            viewModel.fetch(clipSource.value!!)
                        },
                        text = { },
                        icon =  { Icon(
                            painter = painterResource(Res.drawable.icon_get),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        ) },
                        containerColor = MaterialTheme.colorScheme.primaryFixedDim,
                        contentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primaryFixedDim)
                    )
                    FloatingActionButtonMenuItem(
                        onClick = {
                            viewModel.copy()
                        },
                        text = {  },
                        icon =  { Icon(
                            painter = painterResource(Res.drawable.icon_copy),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        ) },
                        containerColor = MaterialTheme.colorScheme.primaryFixedDim,
                        contentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.primaryFixedDim),
                    )
                }
            )
        }
    ) {

        if (clipSource.value == null) {
            MissingSource(backClick = onBackClick)
        } else {
            ActiveClipSourceView(
                clipSource = clipSource.value!!,
                state = clipOperationState.value,
                backClick = {
                    onBackClick()
                },
                modifier = Modifier.padding(it)
            )
        }
    }
}


@Composable
fun MissingSource(backClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = stringResource(Res.string.clip_error_missing_source),
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(Res.string.common_action_go_back),
                fontSize = 32.sp,
                modifier = Modifier.clickable {
                    backClick()
                }
            )
        }
    }
}

@Composable
fun EmptyArea() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(Res.drawable.no_data),
            contentDescription = "image",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ErrorArea(
    error: String
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painterResource(Res.drawable.error),
                contentDescription = "image",
                modifier = Modifier
                    .size(200.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = error,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun ContentArea(
    currentText: String = """
        {
            'data': '123'
        }
    """.trimIndent()) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = currentText,
            fontSize = 22.sp,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun ActiveClipSourceHeader(source: ClipSource, backClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = 8.dp))
            .background(Color(source.color.toLong(16)))
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_back),
                contentDescription = "back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = {
                        backClick()
                    })
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = source.name,
                fontSize = 32.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ActiveClipSourceView(
    clipSource: ClipSource,
    state: ActiveClipViewModel.ActiveClipOperationState,
    backClick: () -> Unit,
    modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        ActiveClipSourceHeader(clipSource, backClick = backClick)
        Spacer(
            modifier = Modifier.size(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {

            when(state) {
                is ActiveClipViewModel.ActiveClipOperationState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(72.dp)
                            .align(Alignment.Center)
                    )
                }

                is ActiveClipViewModel.ActiveClipOperationState.Error -> {
                    ErrorArea(
                        state.error
                    )
                }

                is ActiveClipViewModel.ActiveClipOperationState.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyArea()
                    } else {
                        ContentArea(state.data)
                    }
                }
            }
        }
    }
}



@Composable
fun ActiveClipSourceViewPreview(
    state: ActiveClipViewModel.ActiveClipOperationState
) {
    val source = ClipSource(
        name = "Montana",
        url = "http://localhost",
        color = Color.Red.toHexString()
    )
    ActiveClipSourceView(
        clipSource = source,
        state = state,
        backClick = {},
        modifier = Modifier
    )
}

@Preview
@Composable
fun ActiveClipSourceViewPreview1() {
    ActiveClipSourceViewPreview(state = ActiveClipViewModel.ActiveClipOperationState.Success("new text"))
}

@Preview
@Composable
fun ActiveClipSourceViewPreview2() {
    ActiveClipSourceViewPreview(state = ActiveClipViewModel.ActiveClipOperationState.Loading(currentData = "old text"))
}

@Preview
@Composable
fun ActiveClipSourceViewPreview3() {
    ActiveClipSourceViewPreview(state = ActiveClipViewModel.ActiveClipOperationState.Error(error = "Error loading","new text"))
}


@Preview
@Composable
fun MissingSourcePreview() {
    MissingSource(
        backClick = {}
    )
}