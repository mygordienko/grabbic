package com.mygordienko.grabbic.clip

interface ClipboardOperationService {
    suspend fun read(): String
    suspend fun clear(): Unit
    suspend fun write(data: String): Unit
}