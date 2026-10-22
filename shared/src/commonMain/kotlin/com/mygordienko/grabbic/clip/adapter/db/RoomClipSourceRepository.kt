package com.mygordienko.grabbic.clip.adapter.db

import com.mygordienko.grabbic.clip.ClipSourceRepository
import com.mygordienko.grabbic.clip.domain.ClipSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class RoomClipSourceRepository(
    val clipSourceDatabase: ClipSourceDatabase
): ClipSourceRepository {

    init {
        runBlocking(Dispatchers.IO) {
            val s1 = ClipSource(name = "Montana", url = "http://192.168.1.212:8989", color = "FF9A5800")
            val s2 = ClipSource(name = "Nevada", url = "http://192.168.1.212:8989", color = "FF103C3F")
            clipSourceDatabase.noteDao().insertSource(s1)
            clipSourceDatabase.noteDao().insertSource(s2)
        }
    }

    override suspend fun getAllSources(): List<ClipSource> {
        return clipSourceDatabase.noteDao().getAllSources()
    }

    override suspend fun deleteSource(id: String) {
        clipSourceDatabase.noteDao().deleteSource(id)
    }

    override suspend fun addSource(source: ClipSource) {
        clipSourceDatabase.noteDao().insertSource(source)
    }

    override suspend fun updateSource(source: ClipSource) {
        clipSourceDatabase.noteDao().insertSource(source)
    }
}