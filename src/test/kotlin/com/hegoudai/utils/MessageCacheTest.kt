package com.hegoudai.utils

import com.hegoudai.models.EncryptedMessage
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageCacheTest {
    @Test
    fun testListAddMsgToFixedSizeQueue() = run {
        val testQueue = LinkedList<EncryptedMessage>()
        val message1 = EncryptedMessage("fromPub1", "toPub", "au", "content", "iv", "sig")
        val message2 = EncryptedMessage("fromPub2", "toPub", "au", "content", "iv", "sig")

        testQueue.addMsgToFixedSizeQueue(message1, 1)
        assertEquals(1, testQueue.size)
        assertEquals(message1, testQueue.iterator().next())

        testQueue.addMsgToFixedSizeQueue(message2, 1)
        assertEquals(1, testQueue.size)
        assertEquals(message2, testQueue.iterator().next())
    }

    @Test
    fun testMapAddMsgToQueue() = run {
        val testMap = ConcurrentHashMap<String, LinkedList<EncryptedMessage>>()
        val message1 = EncryptedMessage("fromPub", "toPub1", "au", "content", "iv", "sig")
        val message2 = EncryptedMessage("fromPub", "toPub2", "au", "content", "iv", "sig")

        testMap.addMsgToQueue(message1, 1)
        assertEquals(1, testMap.size)
        assertEquals(message1, testMap[message1.toPub]!![0])

        testMap.addMsgToQueue(message2, 1)
        assertEquals(1, testMap.size)
        assertEquals(message2, testMap[message2.toPub]!![0])
    }
}