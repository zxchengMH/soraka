package com.zxc.soraka.crypto

import java.lang.RuntimeException

class CryptoException : RuntimeException {

    private val serialVersionUID = 8068509879445395353L

    constructor(message: String)
            : super(message)

    constructor(e: Throwable)
            : super(e)

    constructor(message: String, throwable: Throwable)
            : super(message, throwable)
}