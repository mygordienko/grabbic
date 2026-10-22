package com.mygordienko.grabbic.clip

import com.mygordienko.grabbic.clip.domain.ClipSource
import kotlinx.coroutines.flow.Flow

interface ClipSourceRepository {
    fun getAllSources(): Flow<List<ClipSource>>
    suspend fun deleteSource(id: String): Unit
    suspend fun addSource(source: ClipSource): Unit
    suspend fun updateSource(source: ClipSource): Unit
}