package com.hegoudai.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.configureSecurity() {

    authentication {
        jwt {
            val jwtAudience =
                    this@configureSecurity.environment.config.property("jwt.audience").getString()
            val issuer =
                    this@configureSecurity.environment.config.property("jwt.issuer").getString()
            val secret =
                    this@configureSecurity.environment.config.property("jwt.secret").getString()
            authHeader { call -> call.request.authHeaderFromParamsOrHeader() }
            verifier(
                    JWT.require(Algorithm.HMAC256(secret))
                            .withAudience(jwtAudience)
                            .withIssuer(issuer)
                            .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience))
                        JWTPrincipal(credential.payload)
                else null
            }

            challenge { _, _ ->
                call.respond(
                        HttpStatusCode(401, "test custom http"),
                        "Token is not valid or has expired"
                )
            }
        }
    }
}

fun ApplicationRequest.authHeaderFromParamsOrHeader() =
        try {
            // try to get token from params first, for websocket validate
            this.call.parameters["token"]?.let {
                return io.ktor.http.auth.parseAuthorizationHeader(it)
            }

            authorization()?.let { parseAuthorizationHeader() }
        } catch (cause: IllegalArgumentException) {
            // log error
            this.call.application.environment.log.error(cause.message, cause)
            null
        }
