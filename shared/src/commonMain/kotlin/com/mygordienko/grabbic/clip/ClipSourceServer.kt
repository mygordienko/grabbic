package com.mygordienko.grabbic.clip

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface ClipSourceServer {
    fun start(valueProvider: () -> String, scope: CoroutineScope): Unit
    fun stop(): Unit
    val serverState: StateFlow<ServerState>
}

enum class ServerState {
    Stopped,
    Starting,
    Started,
    Stopping,
    Error
}