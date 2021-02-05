package com.zxc.soraka.utils

import java.math.BigDecimal
import java.math.BigInteger
import java.text.NumberFormat

public fun Long.fen2yuan(): String{

    return PriceUtil.finance(PriceUtil.operation(this))
}

public fun Int.fen2yuan(): String{

    return PriceUtil.finance(PriceUtil.operation(this))
}

public fun BigInteger.fen2yuan(): String{

    return PriceUtil.finance(PriceUtil.operation(this))
}

object PriceUtil {

    private var BIG_DECIMAL_100: BigDecimal = BigDecimal(100)

    var BIG_INTEGER_100: BigInteger = BigInteger.valueOf(100)

    /**
     * NumberFormat单线程安全，使用ThreadLocal缓存起来，提高部分效率
     */
    private val NUMBER_FORMAT_LOCAL: ThreadLocal<NumberFormat> = ThreadLocal()

    fun operation(fen: BigInteger): BigDecimal{
        return BigDecimal(fen).divide(BIG_DECIMAL_100)
    }

    fun operation(fen: Long): BigDecimal{
        return BigDecimal(fen).divide(BIG_DECIMAL_100)
    }

    fun operation(fen: Int): BigDecimal{
        return BigDecimal(fen).divide(BIG_DECIMAL_100)
    }

    /**
     * 格式化为财经计数格式
     * @param number
     * @return
     */
    fun finance(number: Any): String {
        NUMBER_FORMAT_LOCAL.get()?.let {
            return it.format(number)
        }
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        var price = numberFormat.format(number)
        NUMBER_FORMAT_LOCAL.set(numberFormat)
        return price
    }
}
