package com.hegoudai.routes

import com.google.gson.Gson
import com.hegoudai.models.EncryptedMessage
import com.hegoudai.plugins.onlines
import com.hegoudai.utils.CryptoUtils
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.websocket.*
import java.security.MessageDigest
import java.util.Base64

fun Route.messageRouting() {
    route("/message") {
        post("/send") {
            val message = call.receive<EncryptedMessage>()
            if (!CryptoUtils.ecSignVerify(
                            CryptoUtils.ecPubFromBytes(Base64.getUrlDecoder().decode(message.fromPub)),
                            MessageDigest.getInstance("SHA-256")
                                    .digest(message.content.toByteArray()),
                            Base64.getDecoder().decode(message.signature)
                    )
            ) {
                // signature error
                call.respondText("Signature error", status = HttpStatusCode.BadRequest)
                return@post
            }
            if (onlines.containsKey(message.toPub)) {
                onlines[message.toPub]!!.send(Gson().toJson(message))
                call.respondText("Message sent", status = HttpStatusCode.OK)
            } else {
                // todo cache message maybe
                call.respondText("Address offline", status = HttpStatusCode.Accepted)
            }
        }
    }
}
