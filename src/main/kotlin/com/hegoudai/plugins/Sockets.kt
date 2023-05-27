package com.hegoudai.plugins

import com.google.gson.Gson
import com.hegoudai.utils.MESSAGE_CACHE
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.logging.*
import io.ktor.websocket.*
import kotlinx.coroutines.async
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

val ONLINES = ConcurrentHashMap<String, DefaultWebSocketSession>()

internal val LOGGER = KtorSimpleLogger("com.hegoudai.plugin.Sockets")
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
                ONLINES[pub] = this

                // send cached messages
                if (MESSAGE_CACHE.containsKey(pub)) {
                    val queue = MESSAGE_CACHE[pub]!!
                    while (queue.isNotEmpty()) {
                        async { this@webSocket.send(Gson().toJson(queue.poll())) }.await()
                    }
                    MESSAGE_CACHE.remove(pub)
                }


                try {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                    }
                } catch (e: Exception) {
                    LOGGER.error(e.localizedMessage)
                } finally {
                    ONLINES.remove(pub)
                }
            }
        }
    }
}
