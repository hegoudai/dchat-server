package com.hegoudai.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.hegoudai.models.EncryptedMessage
import com.hegoudai.routes.messageRouting
import com.hegoudai.utils.CryptoUtils
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.TimeUnit

fun Application.configureRouting() {
    routing {
        messageRouting()

        post("/login") {
            val message = call.receive<EncryptedMessage>()
            // verify login opt
            val config = TimeBasedOneTimePasswordConfig(codeDigits = 6,
                hmacAlgorithm = HmacAlgorithm.SHA256,
                timeStep = 5,
                timeStepUnit = TimeUnit.SECONDS)
            val otp = TimeBasedOneTimePasswordGenerator(message.fromPub.toByteArray(), config).generate()
            if (!CryptoUtils.ecSignVerify(
                    CryptoUtils.ecPubFromBytes(Base64.getUrlDecoder().decode(message.fromPub)),
                    otp.toByteArray(),
                    Base64.getDecoder().decode(message.signature)
                )
            ) {
                call.respondText("Signature error", status = HttpStatusCode.BadRequest)
                return@post
            }
            // generate jwt
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
