package com.mygordienko.grabbic.clip.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Entity(tableName = "clip_sources")
data class ClipSource(
    @PrimaryKey
    val id: String = Uuid.random().toString(),
    val name: String,
    val url: String,
    val color: String,
    val updatedAt: String = Clock.System.now().toString(),
)