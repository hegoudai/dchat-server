package com.hegoudai.models

data class EncryptedMessage(
        val fromAddress: String,
        val toAddress: String,
        val content: String,
        val iv: String,
        val signature: String
)
