package com.mygordienko.grabbic.clip.adapter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mygordienko.grabbic.clip.AddClipSourceViewModel
import com.mygordienko.grabbic.clip.ClipSourceRepository
import com.mygordienko.grabbic.clip.domain.ClipSource
import com.mygordienko.grabbic.common.SevenColorPicker
import com.mygordienko.grabbic.common.ValidatingInputTextField
import com.mygordienko.grabbic.common.toColorFromHex
import com.mygordienko.grabbic.common.toHexString
import com.mygordienko.grabbic.ui.theme.GrabbicTheme
import grabbic.shared.generated.resources.Res
import grabbic.shared.generated.resources.clip_property_name
import grabbic.shared.generated.resources.clip_property_url
import grabbic.shared.generated.resources.clip_source_add_title
import grabbic.shared.generated.resources.clip_source_validation_invalid_name
import grabbic.shared.generated.resources.clip_source_validation_invalid_url
import grabbic.shared.generated.resources.common_action_cancel
import grabbic.shared.generated.resources.common_action_save
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.stringResource

@Composable
fun AddClipSourceScreen(clipSourceRepository: ClipSourceRepository, navController: NavController) {
    val viewModel = viewModel { AddClipSourceViewModel(clipSourceRepository) }

    var name by remember { mutableStateOf("") }
    val nameHasErrors by remember {
        derivedStateOf {
            !name.isNotEmpty()
        }
    }

    var color by remember { mutableStateOf("FF9A5800") }

    var url by remember { mutableStateOf("") }
    val urlRegex = """^https?:\/\/(([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})(\:([-a-zA-Z0-9_]{1,9000}))?|(([-a-zA-Z0-9_]{1,256})(\:([-a-zA-Z0-9_]{1,4}))?))$""".toRegex()
    val urlHasErrors by remember {
        derivedStateOf {
            if (url.isNotEmpty()) {
                !urlRegex.matches(url)
            } else {
                true
            }
        }
    }

    val colors = listOf(
        Color(0xFF123456),
        Color(0xFF9A5800),
        Color(0xFFF26B1D),
        Color(0xFF154C57),
        Color(0xFF60228C),
    )

    val formHasErrors = urlHasErrors || nameHasErrors

    fun saveSource(source: ClipSource) {
        viewModel.addSource(
            clipSource = source,
            onSuccess = {
                navController.popBackStack()
            },
            onError = {}
        )
    }

    fun cancel() {
        navController.popBackStack()
    }

    Scaffold {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text(
                text = stringResource(Res.string.clip_source_add_title),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            ValidatingInputTextField(
                label = {
                    Text(
                        text = stringResource(Res.string.clip_property_name),
                        fontSize = 16.sp
                    )
                },
                value = name,
                updateState = { input -> name = input },
                validatorHasErrors = nameHasErrors,
                errorText = {
                    Text(text = stringResource(Res.string.clip_source_validation_invalid_name))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(
                modifier = Modifier.size(16.dp)
            )

            ValidatingInputTextField(
                label = {
                    Text(
                        text = stringResource(Res.string.clip_property_url),
                        fontSize = 16.sp
                    )
                },
                value = url,
                updateState = { input -> url = input },
                validatorHasErrors = urlHasErrors,
                errorText = {
                    Text(text = stringResource(Res.string.clip_source_validation_invalid_url))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(
                modifier = Modifier.size(16.dp)
            )

            SevenColorPicker(
                colors = colors,
                selectedColor = color.toColorFromHex(),
                onColorSelected = {
                    color = it.toHexString()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.weight(0.85f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(0.15f)
            ) {
                Button(
                    enabled = !formHasErrors,
                    shape = RoundedCornerShape(25f),
                    onClick = {
                        saveSource(ClipSource(name= name, url = url, color = color))
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        fontSize = 16.sp,
                        text = stringResource(Res.string.common_action_save),
                    )
                }
                Button(
                    shape = RoundedCornerShape(25f),
                    onClick = {
                        cancel()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        fontSize = 16.sp,
                        text = stringResource(Res.string.common_action_cancel),
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun AddClipSourceScreenPreview() {
    val repository = object: ClipSourceRepository {
        override fun getAllSources(): Flow<List<ClipSource>> {
            return flowOf(emptyList())
        }

        override suspend fun deleteSource(id: String) {
        }

        override suspend fun addSource(source: ClipSource) {
        }

        override suspend fun updateSource(source: ClipSource) {
        }
    }
    val navController = rememberNavController()

    GrabbicTheme {
        AddClipSourceScreen(repository, navController)
    }
}