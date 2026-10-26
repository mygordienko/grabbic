package com.mygordienko.grabbic.clip.adapter.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mygordienko.grabbic.clip.domain.ClipSource
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipSourceDao {
    @Query("SELECT * FROM clip_sources ORDER BY updatedAt DESC")
    fun getAllSources(): Flow<List<ClipSource>>

    @Query("SELECT * FROM clip_sources WHERE id = :id")
    fun getSourceById(id: String): Flow<ClipSource?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSource(source: ClipSource)

    @Update
    suspend fun updateSource(source: ClipSource)

    @Query(value = "DELETE FROM clip_sources WHERE id = :id")
    suspend fun deleteSource(id: String)
}