package com.mygordienko.grabbic.clip.adapter.remote

import co.touchlab.kermit.Logger
import com.mygordienko.grabbic.clip.ClipSourceServer
import com.mygordienko.grabbic.clip.ServerState
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KtorServerManager(
    private val startupTimeoutMs: Long = 8_000L
): ClipSourceServer {
    private var embeddedServer: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? = null
    private var startupJob: Job? = null

    // TODO: make configurable
    val port: Int = 56001

    private val _serverState = MutableStateFlow(ServerState.Stopped)
    override val serverState: StateFlow<ServerState> = _serverState.asStateFlow()

    override fun start(valueProvider: () -> String, scope: CoroutineScope) {
        if (embeddedServer != null || _serverState.value == ServerState.Starting) return
        Logger.i { "Starting embedded server" }

        _serverState.value = ServerState.Starting

        try {
            embeddedServer = embeddedServer(CIO, port = port) {
                module(valueProvider)
            }.start(wait = false)
        } catch (e: Exception) {
            _serverState.value = ServerState.Error
            embeddedServer = null
            Logger.e(throwable = e) { "Exception starting embedded server" }
            return
        }

        // if we're still "Starting" after the timeout, treat it as failure
        startupJob = scope.launch {
            delay(startupTimeoutMs)
            if (_serverState.value == ServerState.Starting) {
                Logger.e { "Embedded server takes too long to start, cancelling" }
                _serverState.value = ServerState.Error
                runCatching { embeddedServer?.stop(1000, 3000) }
                embeddedServer = null
            }
        }
    }

    override fun stop() {
        Logger.i { "Stopping embedded server" }
        startupJob?.cancel()
        startupJob = null

        _serverState.value = ServerState.Stopping
        try {
            embeddedServer?.stop(1000, 3000)
        } finally {
            embeddedServer = null
            _serverState.value = ServerState.Stopped
        }
    }

    private fun Application.module(valueProvider: () -> String) {
        monitor.subscribe(ApplicationStarted) {
            startupJob?.cancel()
            startupJob = null
            _serverState.value = ServerState.Started
            Logger.i { "Embedded server started" }
        }

        monitor.subscribe(ApplicationStopped) {
            _serverState.value = ServerState.Stopped
        }

        routing {
            get(CLIPBOARD_PATH) {
                val snapshot = valueProvider()
                call.respondText(snapshot)
            }
        }
    }
}