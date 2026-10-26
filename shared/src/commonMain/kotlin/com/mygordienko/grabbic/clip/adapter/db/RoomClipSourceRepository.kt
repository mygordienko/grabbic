package com.mygordienko.grabbic.clip.adapter.db

import com.mygordienko.grabbic.clip.ClipSourceRepository
import com.mygordienko.grabbic.clip.domain.ClipSource
import kotlinx.coroutines.flow.Flow

class RoomClipSourceRepository(
    val clipSourceDatabase: ClipSourceDatabase
): ClipSourceRepository {

    override fun getAllSources(): Flow<List<ClipSource>> {
        return clipSourceDatabase.clipSourceDao().getAllSources()
    }

    override fun getById(id: String): Flow<ClipSource?> {
        return clipSourceDatabase.clipSourceDao().getSourceById(id)
    }

    override suspend fun deleteSource(id: String) {
        clipSourceDatabase.clipSourceDao().deleteSource(id)
    }

    override suspend fun addSource(source: ClipSource) {
        clipSourceDatabase.clipSourceDao().insertSource(source)
    }

    override suspend fun updateSource(source: ClipSource) {
        clipSourceDatabase.clipSourceDao().insertSource(source)
    }
}