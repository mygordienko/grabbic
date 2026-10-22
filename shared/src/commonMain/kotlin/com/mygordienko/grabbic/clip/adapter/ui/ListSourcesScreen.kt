package com.mygordienko.grabbic.clip.adapter.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mygordienko.grabbic.clip.domain.ClipSource
import grabbic.shared.generated.resources.Res
import grabbic.shared.generated.resources.delete
import org.jetbrains.compose.resources.painterResource

@Composable
fun ListSourcesScreen(notes: List<ClipSource>, onDelete: (ClipSource) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = notes) {
            ClipSourceItem(source = it, onDelete)
        }
    }
}
@Composable
fun ClipSourceItem(source: ClipSource, onDelete: (ClipSource) -> Unit) {
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
            Text(
                text = source.name,
                fontSize = 32.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Image(
                painter = painterResource(Res.drawable.delete),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onDelete.invoke(source)
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListNoteScreenPreview() {
    ListSourcesScreen(listOf(
        ClipSource(name = "Montana", url = "http://192.168.1.212:8989", color = "FF9A5800"),
        ClipSource(name = "Nevada", url = "http://192.168.1.212:8989", color = "FF103C3F")
    ), {})
}