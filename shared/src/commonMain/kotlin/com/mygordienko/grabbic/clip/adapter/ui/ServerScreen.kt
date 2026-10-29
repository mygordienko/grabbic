package com.mygordienko.grabbic.clip.adapter.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mygordienko.grabbic.clip.ClipSourceServer
import com.mygordienko.grabbic.clip.ClipboardOperationService
import com.mygordienko.grabbic.clip.ServerState
import com.mygordienko.grabbic.clip.ServerViewModel
import com.mygordienko.grabbic.clip.ServerViewModel.ServerUiClipboardState
import grabbic.shared.generated.resources.Res
import grabbic.shared.generated.resources.icon_arrow_back
import grabbic.shared.generated.resources.icon_refresh
import grabbic.shared.generated.resources.server_action_start
import grabbic.shared.generated.resources.server_action_stop
import grabbic.shared.generated.resources.server_status_started
import grabbic.shared.generated.resources.server_status_starting
import grabbic.shared.generated.resources.server_status_stopped
import grabbic.shared.generated.resources.server_status_stopping
import grabbic.shared.generated.resources.server_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ServerScreen(
    clipboardServer: ClipSourceServer,
    clipboardOperationService: ClipboardOperationService,
    onBackClick: () -> Unit
    ) {

    val viewModel = viewModel { ServerViewModel(clipboardServer, clipboardOperationService) }
    val serverState = viewModel.serverOperationState.collectAsState()
    val clipboardState = viewModel.clipboardOperationState.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopServer()
        }
    }

    Scaffold {
        ServerParentContainer(
            paddingValues = it,
            onStartServerClick = {
                viewModel.startServer()
            },
            onStopServerClick = {
                viewModel.stopServer()
            },
            onReloadClick = {
                viewModel.reload()
            },
            onBackClick = {
                onBackClick()
            },
            serverState = serverState.value,
            clipState = clipboardState.value
        )
    }
}


@Composable
fun ServerParentContainer(
    paddingValues: PaddingValues,
    serverState: ServerState,
    clipState: ServerUiClipboardState,
    onBackClick: () -> Unit,
    onReloadClick: () -> Unit,
    onStartServerClick: () -> Unit,
    onStopServerClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        ServerHeader(
            onBackClick = onBackClick,
            serverState = serverState
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            ServerClipboardContentArea(
                currentText = clipState.data
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                enabled = (serverState == ServerState.Stopped || serverState == ServerState.Error),
                shape = RoundedCornerShape(25f),
                onClick = {
                    onStartServerClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = stringResource(Res.string.server_action_start),
                )
            }
            Button(
                enabled = (serverState == ServerState.Started || serverState == ServerState.Starting),
                shape = RoundedCornerShape(25f),
                onClick = {
                    onStopServerClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = stringResource(Res.string.server_action_stop),
                )
            }
            Button(
                shape = RoundedCornerShape(25f),
                onClick = {
                    onReloadClick()
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.icon_refresh),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

    }
}


@Preview
@Composable
fun ServerParentContainerPreview() {
    ServerParentContainer(
        paddingValues = PaddingValues(8.dp),
        onBackClick = {},
        onReloadClick = {},
        onStartServerClick = {},
        onStopServerClick = {},
        serverState = ServerState.Starting,
        clipState = ServerUiClipboardState.Success("data")
    )
}

@Composable
fun ServerHeader(
    serverState: ServerState,
    onBackClick: () -> Unit,
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
            text = stringResource(Res.string.server_title),
            fontSize = 30.sp,
            modifier = Modifier
        )
        Spacer(Modifier.size(16.dp))

        var statusMessage = stringResource(Res.string.server_status_stopped)
        var statusMessageColor = Color(0xFF103C3F)
        when(serverState) {
            ServerState.Stopped -> {
                statusMessage = stringResource(Res.string.server_status_stopped)
                statusMessageColor = Color(0xFF103C3F)
            }
            ServerState.Starting -> {
                statusMessage = stringResource(Res.string.server_status_starting)
                statusMessageColor = Color(0xFF103C3F)
            }
            ServerState.Started -> {
                statusMessage = stringResource(Res.string.server_status_started)
                statusMessageColor = Color(0xFF66FF66)
            }
            ServerState.Stopping -> {
                statusMessage = stringResource(Res.string.server_status_stopping)
                statusMessageColor = Color(0xFF103C3F)
            }
            ServerState.Error -> {
                statusMessage = stringResource(Res.string.server_status_stopped)
                statusMessageColor = Color(0xFFFF6666)
            }
        }
        Text(
            text = statusMessage,
            color = statusMessageColor,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
        )

    }
}

@Composable
fun ServerClipboardContentArea(
    currentText: String
) {
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