package com.mygordienko.grabbic.clip.adapter.remote

import com.mygordienko.grabbic.clip.ClipSourceClient
import com.mygordienko.grabbic.clip.domain.ClipSource

class DummyClipSourceClient: ClipSourceClient {
    override suspend fun fetch(clipSource: ClipSource): String {
        return "Data from ${clipSource.name}"
    }
}