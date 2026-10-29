package com.mygordienko.grabbic.clip.adapter.remote

import co.touchlab.kermit.Logger
import com.mygordienko.grabbic.clip.ClipSourceClient
import com.mygordienko.grabbic.clip.domain.ClipSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom

class HttpClipSourceClient(val client: HttpClient): ClipSourceClient  {

    override suspend fun fetch(clipSource: ClipSource): String {
        try {
            val url = URLBuilder().apply {
                takeFrom(clipSource.url)
                appendPathSegments(CLIPBOARD_PATH)
            }.build().toString()

            val response = client.get(url)

            if (response.status == HttpStatusCode.OK) {
                return response.bodyAsText()
            } else {
                Logger.e { "Error response from remote ${clipSource.name}" }
                throw Exception("Error response from source ${response.status}")
            }
        } catch (ex: Exception) {
            Logger.e(throwable = ex) { "Exception fetching data from remote ${clipSource.name}" }
            throw Exception("Failed to fetch data")
        }
    }
}