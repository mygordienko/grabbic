package com.mygordienko.grabbic.clip

import com.mygordienko.grabbic.clip.domain.ClipSource
import kotlinx.coroutines.flow.Flow

interface ClipSourceRepository {
    suspend fun getAllSources(): List<ClipSource>
    suspend fun deleteSource(id: String): Unit
    suspend fun addSource(source: ClipSource): Unit
    suspend fun updateSource(source: ClipSource): Unit
}