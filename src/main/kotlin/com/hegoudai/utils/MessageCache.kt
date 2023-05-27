package com.hegoudai.utils

import com.hegoudai.models.EncryptedMessage
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap

// cache for user's messages
val MESSAGE_CACHE = ConcurrentHashMap<String, LinkedList<EncryptedMessage>>()

// cache fixed size user's message
fun ConcurrentHashMap<String, LinkedList<EncryptedMessage>>.addMsgToQueue(message: EncryptedMessage, fixedSize: Int) {
    if (!this.containsKey(message.toPub)) {
        if (this.size == fixedSize) {
            // remove an unlucky guy, haha
            this.remove(this.keys().iterator().next())
        }
        this[message.toPub] = LinkedList()
    }
    this[message.toPub]?.addMsgToFixedSizeQueue(message, 20)
}

// cache fixed size messages for a user
fun LinkedList<EncryptedMessage>.addMsgToFixedSizeQueue(message: EncryptedMessage, fixedSize: Int) {
    if (this.size == fixedSize) {
        this.remove()
    }
    this.add(message)
}