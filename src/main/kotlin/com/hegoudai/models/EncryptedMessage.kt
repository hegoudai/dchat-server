package com.hegoudai.models

data class EncryptedMessage(
        val fromPub: String,
        val toPub: String,
        val authority: String,
        val content: String,
        val iv: String,
        val signature: String
)
