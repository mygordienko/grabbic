package com.mygordienko.grabbic.clip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mygordienko.grabbic.clip.domain.ClipSource
import kotlinx.coroutines.launch

class AddClipSourceViewModel(val clipSourceRepository: ClipSourceRepository): ViewModel() {

    fun addSource(clipSource: ClipSource, onSuccess: () -> Unit, onError: () -> Unit) {
        // TODO: use "uiState" the corresponding Screen can rely on
        viewModelScope.launch {
            try {
                clipSourceRepository.addSource(clipSource)
                onSuccess()
            } catch (ex: Exception) {
                onError()
            }
        }
    }
}