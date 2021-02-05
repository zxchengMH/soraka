package com.zxc.soraka.crypto.symmetric

enum class SymmetricAlgorithm(private var value: String) {

    /** 默认的AES加密方式：AES/CBC/PKCS5Padding  */
    AES("AES"),

    ARCFOUR("ARCFOUR"),

    Blowfish("Blowfish"),

    /** 默认的DES加密方式：DES/ECB/PKCS5Padding  */
    DES("DES"),

    /** 3DES算法，默认实现为：DESede/CBC/PKCS5Padding  */
    DESede("DESede"),

    RC2("RC2"),

    PBEWithMD5AndDES("PBEWithMD5AndDES"),

    PBEWithSHA1AndDESede("PBEWithSHA1AndDESede"),

    PBEWithSHA1AndRC2_40("PBEWithSHA1AndRC2_40");


    /**
     * 构造
     * @param value 算法的字符串表示，区分大小写
     */
    private fun SymmetricAlgorithm(value: String) {
        this.value = value
    }

    /**
     * 获得算法的字符串表示形式
     * @return 算法字符串
     */
    fun getValue(): String {
        return this.value
    }
}