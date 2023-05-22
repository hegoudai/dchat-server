package com.hegoudai.models

import com.hegoudai.utils.CryptoUtils
import java.util.*
import org.bouncycastle.asn1.ASN1BitString
import org.bouncycastle.asn1.ASN1InputStream
import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.ASN1UTF8String
import org.bouncycastle.jce.interfaces.ECPublicKey

class AddressInfos(val server: String, val pub: ECPublicKey) {
    companion object {
        fun fromAddress(address: String): AddressInfos {
            // @TODO validate address
            val input = ASN1InputStream(Base64.getUrlDecoder().decode(address))
            val p = input.readObject()
            val sequence = ASN1Sequence.getInstance(p)
            val server = ASN1UTF8String.getInstance(sequence.getObjectAt(0)).string
            val pubBytes = ASN1BitString.getInstance(sequence.getObjectAt(1)).bytes
            return AddressInfos(server, CryptoUtils.ecPubFromBytes(pubBytes))
        }
    }
}
