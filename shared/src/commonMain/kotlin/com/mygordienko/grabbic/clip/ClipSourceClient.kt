package com.mygordienko.grabbic.clip

import com.mygordienko.grabbic.clip.domain.ClipSource

class ClipSourceClient {

    suspend fun fetch(clipSource: ClipSource): String {
        return "Data from ${clipSource.name}"
    }
}