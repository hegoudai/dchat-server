package com.hegoudai.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

val onlines = ConcurrentHashMap<String, DefaultWebSocketSession>()

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        authenticate {
            webSocket("/chat") {
                // add to onlines
                val pub = call.principal<JWTPrincipal>()!!["pub"]!!
                onlines[pub] = this
                try {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    onlines.remove(pub)
                }
            }
        }
    }
}
