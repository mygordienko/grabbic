package com.mygordienko.grabbic.clip.adapter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import grabbic.shared.generated.resources.Res
import grabbic.shared.generated.resources.home_mode_client_title
import grabbic.shared.generated.resources.home_mode_server_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    navigateToClient: ()-> Unit,
    navigateToServer: ()-> Unit
) {
    Scaffold() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ModeItem(
                text = stringResource(Res.string.home_mode_client_title),
                color = Color(0xFF38729E),
                onClick = navigateToClient
            )
            Spacer(modifier = Modifier.size(16.dp))
            ModeItem(
                text = stringResource(Res.string.home_mode_server_title),
                color = Color(0xFF9E4D17),
                onClick = navigateToServer
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navigateToClient = {},
        navigateToServer = {}
    )
}

@Composable
fun ModeItem(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = 8.dp))
            .background(color)
            .clickable {
                onClick.invoke()
            }
            .padding(28.dp)
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.contentColorFor(color)
        )
    }
}