package com.mygordienko.grabbic.clip

import com.mygordienko.grabbic.clip.domain.ClipSource

interface ClipSourceClient {
    suspend fun fetch(clipSource: ClipSource): String
}