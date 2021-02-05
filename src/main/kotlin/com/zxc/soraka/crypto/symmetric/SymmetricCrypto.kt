package com.zxc.soraka.crypto.symmetric

import cn.hutool.crypto.CryptoException
import java.lang.NullPointerException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey

open class SymmetricCrypto {

    private var algorithm: SymmetricAlgorithm? = null

    /** SecretKey 负责保存对称密钥  */
    private var secretKey: SecretKey? = null

    /** Cipher负责完成加密或解密工作  */
    private var cipher: Cipher? = null

    constructor(){

    }

    constructor(algorithm: SymmetricAlgorithm, secretKey: SecretKey){
        this.secretKey = secretKey
        this.algorithm = algorithm
        try {
            this.cipher = Cipher.getInstance(algorithm.getValue())
        } catch (e: Exception) {
            throw CryptoException(e)
        }
    }

    @Synchronized fun encrypt(content: ByteArray): ByteArray?{
        notNull(cipher)
        try {
            cipher!!.init(Cipher.ENCRYPT_MODE, secretKey)
            return cipher!!.doFinal(content)
        }catch (e: Exception){
            throw CryptoException(e)
        }
    }

    @Synchronized fun decrypt(content: ByteArray): ByteArray?{
        notNull(cipher)
        try {
            cipher!!.init(Cipher.DECRYPT_MODE, secretKey)
            return cipher!!.doFinal(content)
        }catch (e: Exception){
            throw CryptoException(e)
        }
    }

    /**
     * 加密
     *
     * @param content 数据
     * @return 加密后的Base64
     * @since 4.0.1
     */
    fun encryptBase64(content: String): String {

        return Base64.getEncoder().encodeToString(encrypt(content.toByteArray()))
    }

    /**
     * 解密为字符串
     *
     * @param content 被解密的
     * @return 解密后的String
     */
    fun decryptBase64(content: String): String? {
        return String(decrypt(Base64.getDecoder().decode(content))!!)
    }

    private fun notNull(cipher: Cipher?){
        if(cipher == null){
            throw NullPointerException("Cipher is null")
        }
    }
}