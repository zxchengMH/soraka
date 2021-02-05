package com.zxc.soraka.utils

import java.text.NumberFormat



/**
 * @Description 数值格式化工具类
 * @Author zxcheng - 1213328214@qq.com
 * @Version 1.0
 */
object NumberFormatUtil {

    private const val PERCENTAGE = "%"

    /**
     * NumberFormat单线程安全，使用ThreadLocal缓存起来，提高部分效率
     */
    private val NUMBER_FORMAT_LOCAL: ThreadLocal<NumberFormat> = ThreadLocal()

    /**
     * 百分比格式化,精确位数2位
     * @param number
     * @return
     */
    fun percentage(number: Number): String {

        NUMBER_FORMAT_LOCAL.get()?.let {
            return it.format(number) + PERCENTAGE
        }
        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        NUMBER_FORMAT_LOCAL.set(numberFormat)
        return numberFormat.format(number) + PERCENTAGE
    }

    /**
     * 百分比格式化
     * @param number
     * @param scale 精确位数
     * @return
     */
    fun percentage(number: Number, scale: Int): String {

        val numberFormat = NumberFormat.getInstance()
        numberFormat.minimumFractionDigits = scale
        numberFormat.maximumFractionDigits = scale
        val result = numberFormat.format(number)
        return result + PERCENTAGE
    }


}