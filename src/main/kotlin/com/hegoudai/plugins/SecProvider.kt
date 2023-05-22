package com.hegoudai.plugins

import io.ktor.server.application.*
import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider

fun Application.configSecProvider() {
    Security.addProvider(BouncyCastleProvider())
}
