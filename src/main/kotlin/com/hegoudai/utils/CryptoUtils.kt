package com.hegoudai.utils

import java.security.PublicKey
import java.security.Signature
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.interfaces.ECPublicKey
import org.bouncycastle.jce.provider.JCEECPublicKey
import org.bouncycastle.jce.spec.ECPublicKeySpec

class CryptoUtils {
    companion object {
        fun ecPubFromBytes(pubBytes: ByteArray): ECPublicKey {
            // secp128r2 curve
            val curveParams = ECNamedCurveTable.getParameterSpec("secp128r2")
            val pubPoint = curveParams.curve.decodePoint(pubBytes)
            return JCEECPublicKey("EC", ECPublicKeySpec(pubPoint, curveParams))
        }

        fun ecSignVerify(pub: PublicKey, signedData: ByteArray, signature: ByteArray): Boolean {
            val sigInstance = Signature.getInstance("ECDSA", "BC")
            sigInstance.initVerify(pub)
            sigInstance.update(signedData)
            return sigInstance.verify(signature)
        }
    }
}
