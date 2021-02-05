package com.zxc.soraka.crypto.symmetric

import java.security.SecureRandom
import javax.crypto.*

class AES: SymmetricCrypto {

    companion object{
        private val AES = SymmetricAlgorithm.AES.getValue()

        private fun generateKey(secret: String): SecretKey {
            val keyGenerator = KeyGenerator.getInstance(AES)
            keyGenerator.init(128, SecureRandom(secret.toByteArray()))
            return keyGenerator.generateKey()
        }
    }

    constructor(secret: String): super(SymmetricAlgorithm.AES, generateKey(secret))

}

fun main() {
    var aes = AES("DADIWKDNAKSBDKQW")
    aes.encryptBase64("zxcdqidhqujasudqwidbkauishdi").let {
        println(it)
        println(aes.decryptBase64(it))
    }
}