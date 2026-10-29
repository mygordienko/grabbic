package com.mygordienko.grabbic.clip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServerViewModel(
    val clipboardServer: ClipSourceServer,
    val clipboardOperationService: ClipboardOperationService
): ViewModel() {
    private val _uiState =
        MutableStateFlow<ServerUiClipboardState>(value = ServerUiClipboardState.Success(""))
    val clipboardOperationState = _uiState.asStateFlow()
    val serverOperationState = clipboardServer.serverState

    override fun onCleared() {
        clipboardServer.stop()
    }

    fun startServer(): Unit {
        viewModelScope.launch(Dispatchers.IO) {
            clipboardServer.start({_uiState.value.data}, viewModelScope)
        }
    }

    fun stopServer(): Unit {
        viewModelScope.launch(Dispatchers.IO) {
            clipboardServer.stop()
        }
    }

    fun reload(): Unit {
        viewModelScope.launch() {
            try {
                _uiState.value = ServerUiClipboardState.Loading(_uiState.value.data)
                val data = clipboardOperationService.read()
                _uiState.value = ServerUiClipboardState.Success(newData = data)
            } catch (ex: Exception) {
                _uiState.value = ServerUiClipboardState.Error(error = "Error reloading clipboard", text = _uiState.value.data)
            }
        }
    }

    sealed class ServerUiClipboardState(val data: String) {
        data class Loading(val text: String): ServerUiClipboardState(text)
        data class Success(val newData: String): ServerUiClipboardState(newData)
        data class Error(val text: String, val error: String): ServerUiClipboardState(text)
    }
}