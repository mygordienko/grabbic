package com.mygordienko.grabbic.clip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mygordienko.grabbic.clip.domain.ClipSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActiveClipViewModel(
    val clipSourceRepository: ClipSourceRepository,
    val clipSourceClient: ClipSourceClient,
    val clipOperationService: ClipboardOperationService
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<ActiveClipOperationState>(value = ActiveClipOperationState.Success(""))
    val clipOperationState = _uiState.asStateFlow()

    fun loadSource(id: String): Flow<ClipSource?> {
        return clipSourceRepository.getById(id)
    }

    fun fetch(clipSource: ClipSource): Unit {
        viewModelScope.launch {
            try {
                _uiState.value = ActiveClipOperationState.Loading(_uiState.value.data)
                val data = clipSourceClient.fetch(clipSource)
                _uiState.value = ActiveClipOperationState.Success(newData = data)
            } catch (ex: Exception) {
                _uiState.value =
                    ActiveClipOperationState.Error(error = "Error fetching clipboard from the source", _uiState.value.data)
            }
        }
    }

    fun clear(): Unit {
        viewModelScope.launch {
            try {
                _uiState.value = ActiveClipOperationState.Loading(_uiState.value.data)
                clipOperationService.clear()
                _uiState.value = ActiveClipOperationState.Success("")
            } catch (ex: Exception) {
                _uiState.value = ActiveClipOperationState.Error(error = "Error clearing clipboard", _uiState.value.data)
            }
        }
    }

    fun reload(): Unit {
        viewModelScope.launch {
            try {
                _uiState.value = ActiveClipOperationState.Loading(_uiState.value.data)
                val data = clipOperationService.read()
                _uiState.value = ActiveClipOperationState.Success(newData = data)
            } catch (ex: Exception) {
                _uiState.value = ActiveClipOperationState.Error(error = "Error reloading clipboard", _uiState.value.data)
            }
        }
    }

    fun copy(): Unit {
        viewModelScope.launch {
            try {
                clipOperationService.write(_uiState.value.data)
            } catch (ex: Exception) {
                _uiState.value =
                    ActiveClipOperationState.Error(error = "Error copying to clipboard", _uiState.value.data)
            }
        }
    }

    sealed class ActiveClipOperationState(val data: String) {
        data class Loading(val currentData: String) : ActiveClipOperationState(currentData)
        data class Success(val newData: String) : ActiveClipOperationState(newData)
        data class Error(val error: String, val currentData: String) : ActiveClipOperationState(currentData)
    }
}