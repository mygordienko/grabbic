package com.mygordienko.grabbic.clip

import androidx.compose.runtime.mutableStateOf

class DummyClipboardOperationService: ClipboardOperationService {
    private val _state = mutableStateOf("")

    override suspend fun read(): String {
        return _state.value
    }

    override suspend fun clear() {
        _state.value = ""
    }

    override suspend fun write(data: String) {
        _state.value = data
    }
}