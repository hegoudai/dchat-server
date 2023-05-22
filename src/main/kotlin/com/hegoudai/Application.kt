package com.hegoudai

import com.hegoudai.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress(
        "unused"
) // application.conf references the main function. This annotation prevents the IDE from marking it
// as unused.
fun Application.module() {
    configSecProvider()
    configureSecurity()
    configureSerialization()
    configureSockets()
    configureHTTP()
    configureRouting()
}
