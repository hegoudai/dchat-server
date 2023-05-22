package com.hegoudai.models

import com.hegoudai.utils.CryptoUtils
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertEquals


class AddressInfosTest {
    @Test
    fun testFromAddress() = run {
        val address = "MC8MCTEyNy4wLjAuMQMiAAQ0lS6EBku3huCUMI0PfKMfp9o539hXOJP2asAr0Ih7_Q=="
        val pub = CryptoUtils.ecPubFromBytes(Base64.getUrlDecoder().decode("BDSVLoQGS7eG4JQwjQ98ox-n2jnf2Fc4k_ZqwCvQiHv9"))
        val addressInfos = AddressInfos.fromAddress(address)
        assertEquals(addressInfos.server, "127.0.0.1")
        assertEquals(addressInfos.pub, pub)
    }
}