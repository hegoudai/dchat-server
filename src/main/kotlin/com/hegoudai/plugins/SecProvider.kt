package com.hegoudai.plugins

import java.security.Security
import org.bouncycastle.jce.provider.BouncyCastleProvider

fun configSecProvider() {
    Security.addProvider(BouncyCastleProvider())
}
