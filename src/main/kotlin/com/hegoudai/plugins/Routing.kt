package com.hegoudai.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.hegoudai.models.EncryptedMessage
import com.hegoudai.routes.messageRouting
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        messageRouting()

        post("/login") {
            // @todo verify login
            val message = call.receive<EncryptedMessage>()
            val secret = this@configureRouting.environment.config.property("jwt.secret").getString()
            val issuer = this@configureRouting.environment.config.property("jwt.issuer").getString()
            val audience =
                    this@configureRouting.environment.config.property("jwt.audience").getString()
            val token =
                    JWT.create()
                            .withAudience(audience)
                            .withIssuer(issuer)
                            .withClaim("pub", message.fromPub)
                            .sign(Algorithm.HMAC256(secret))
            call.respond(hashMapOf("token" to token))
        }
    }
}
