package com.zxc.soraka.utils

import cn.hutool.core.util.CharsetUtil
import cn.hutool.core.util.StrUtil
import java.awt.Color
import java.nio.charset.Charset

object HexUtil {

    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private val DIGITS_LOWER =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private val DIGITS_UPPER =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    /**
     * 判断给定字符串是否为16进制数<br></br>
     * 如果是，需要使用对应数字类型对象的`decode`方法解码<br></br>
     * 例如：`Integer.decode`方法解码int类型的16进制数字
     *
     * @param value 值
     * @return 是否为16进制
     */
    fun isHexNumber(value: String): Boolean {
        val index = if (value.startsWith("-")) 1 else 0
        return value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index)
    }

    // ---------------------------------------------------------------------------------------------------- encode
    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @return 十六进制char[]
     */
    fun encodeHex(data: ByteArray): CharArray {
        return encodeHex(data, true)
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param str 字符串
     * @param charset 编码
     * @return 十六进制char[]
     */
    fun encodeHex(str: String, charset: Charset): CharArray {
        return encodeHex(StrUtil.bytes(str, charset), true)
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @param toLowerCase `true` 传换成小写格式 ， `false` 传换成大写格式
     * @return 十六进制char[]
     */
    fun encodeHex(data: ByteArray, toLowerCase: Boolean): CharArray {
        return encodeHex(data, if (toLowerCase) DIGITS_LOWER else DIGITS_UPPER)
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @return 十六进制String
     */
    fun encodeHexStr(data: ByteArray): String {
        return encodeHexStr(data, true)
    }

    /**
     * 将字节数组转换为十六进制字符串，结果为小写
     *
     * @param data 被编码的字符串
     * @param charset 编码
     * @return 十六进制String
     */
    fun encodeHexStr(data: String, charset: Charset): String {
        return encodeHexStr(StrUtil.bytes(data, charset), true)
    }

    /**
     * 将字节数组转换为十六进制字符串，结果为小写，默认编码是UTF-8
     *
     * @param data 被编码的字符串
     * @return 十六进制String
     */
    fun encodeHexStr(data: String): String {
        return encodeHexStr(data, CharsetUtil.CHARSET_UTF_8)
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @param toLowerCase `true` 传换成小写格式 ， `false` 传换成大写格式
     * @return 十六进制String
     */
    fun encodeHexStr(data: ByteArray, toLowerCase: Boolean): String {
        return encodeHexStr(data, if (toLowerCase) DIGITS_LOWER else DIGITS_UPPER)
    }

    // ---------------------------------------------------------------------------------------------------- decode
    /**
     * 将十六进制字符数组转换为字符串，默认编码UTF-8
     *
     * @param hexStr 十六进制String
     * @return 字符串
     */
    fun decodeHexStr(hexStr: String): String? {
        return decodeHexStr(hexStr, CharsetUtil.CHARSET_UTF_8)
    }

    /**
     * 将十六进制字符数组转换为字符串
     *
     * @param hexStr 十六进制String
     * @param charset 编码
     * @return 字符串
     */
    fun decodeHexStr(hexStr: String, charset: Charset): String? {
        return if (StrUtil.isEmpty(hexStr)) {
            hexStr
        } else decodeHexStr(hexStr.toCharArray(), charset)
    }

    /**
     * 将十六进制字符数组转换为字符串
     *
     * @param hexData 十六进制char[]
     * @param charset 编码
     * @return 字符串
     */
    fun decodeHexStr(hexData: CharArray, charset: Charset): String {
        return StrUtil.str(decodeHex(hexData), charset)
    }

    /**
     * 将十六进制字符数组转换为字节数组
     *
     * @param hexData 十六进制char[]
     * @return byte[]
     * @throws RuntimeException 如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
    fun decodeHex(hexData: CharArray): ByteArray {

        val len = hexData.size

        if (len and 0x01 != 0) {
            throw RuntimeException("Odd number of characters.")
        }

        val out = ByteArray(len shr 1)

        // two characters form the hex value.
        var i = 0
        var j = 0
        while (j < len) {
            var f = toDigit(hexData[j], j) shl 4
            j++
            f = f or toDigit(hexData[j], j)
            j++
            out[i] = (f and 0xFF).toByte()
            i++
        }

        return out
    }

    /**
     * 将十六进制字符串解码为byte[]
     *
     * @param hexStr 十六进制String
     * @return byte[]
     */
    fun decodeHex(hexStr: String): ByteArray? {
        return if (StrUtil.isEmpty(hexStr)) {
            null
        } else decodeHex(hexStr.toCharArray())
    }

    // ---------------------------------------------------------------------------------------- Color
    /**
     * 将[Color]编码为Hex形式
     *
     * @param color [Color]
     * @return Hex字符串
     * @since 3.0.8
     */
    fun encodeColor(color: Color): String {
        return encodeColor(color, "#")
    }

    /**
     * 将[Color]编码为Hex形式
     *
     * @param color [Color]
     * @param prefix 前缀字符串，可以是#、0x等
     * @return Hex字符串
     * @since 3.0.8
     */
    fun encodeColor(color: Color, prefix: String): String {
        val builder = StringBuffer(prefix)
        var colorHex: String
        colorHex = Integer.toHexString(color.red)
        if (1 == colorHex.length) {
            builder.append('0')
        }
        builder.append(colorHex)
        colorHex = Integer.toHexString(color.green)
        if (1 == colorHex.length) {
            builder.append('0')
        }
        builder.append(colorHex)
        colorHex = Integer.toHexString(color.blue)
        if (1 == colorHex.length) {
            builder.append('0')
        }
        builder.append(colorHex)
        return builder.toString()
    }

    /**
     * 将Hex颜色值转为
     *
     * @param hexColor 16进制颜色值，可以以#开头，也可以用0x开头
     * @return [Color]
     * @since 3.0.8
     */
    fun decodeColor(hexColor: String): Color {
        return Color.decode(hexColor)
    }

    /**
     * 将指定int值转换为Unicode字符串形式，常用于特殊字符（例如汉字）转Unicode形式<br></br>
     * 转换的字符串如果u后不足4位，则前面用0填充，例如：
     *
     * <pre>
     * '我' =》\u4f60
    </pre> *
     *
     * @param value int值，也可以是char
     * @return Unicode表现形式
     */
    fun toUnicodeHex(value: Int): String {
        val builder = StringBuilder(6)

        builder.append("\\u")
        val hex = Integer.toHexString(value)
        val len = hex.length
        if (len < 4) {
            builder.append("0000", 0, 4 - len)// 不足4位补0
        }
        builder.append(hex)

        return builder.toString()
    }

    /**
     * 将指定char值转换为Unicode字符串形式，常用于特殊字符（例如汉字）转Unicode形式<br></br>
     * 转换的字符串如果u后不足4位，则前面用0填充，例如：
     *
     * <pre>
     * '我' =》\u4f60
    </pre> *
     *
     * @param ch char值
     * @return Unicode表现形式
     * @since 4.0.1
     */
    fun toUnicodeHex(ch: Char): String {
        val sb = StringBuilder(6)
        sb.append("\\u")
        sb.append(DIGITS_LOWER[ch.toInt() shr 12 and 15])
        sb.append(DIGITS_LOWER[ch.toInt() shr 8 and 15])
        sb.append(DIGITS_LOWER[ch.toInt() shr 4 and 15])
        sb.append(DIGITS_LOWER[ch.toInt() and 15])
        return sb.toString()
    }

    // ---------------------------------------------------------------------------------------- Private method start
    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制String
     */
    private fun encodeHexStr(data: ByteArray, toDigits: CharArray): String {
        return String(encodeHex(data, toDigits))
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    private fun encodeHex(data: ByteArray, toDigits: CharArray): CharArray {
        val l = data.size
        val out = CharArray(l shl 1)
        // two characters from the hex value.
        var i = 0
        var j = 0
        while (i < l) {
            out[j++] = toDigits[(0xF0 and data[i].toInt()).ushr(4)]
            out[j++] = toDigits[0x0F and data[i].toInt()]
            i++
        }
        return out
    }

    /**
     * 将十六进制字符转换成一个整数
     *
     * @param ch 十六进制char
     * @param index 十六进制字符在字符数组中的位置
     * @return 一个整数
     * @throws RuntimeException 当ch不是一个合法的十六进制字符时，抛出运行时异常
     */
    private fun toDigit(ch: Char, index: Int): Int {
        val digit = Character.digit(ch, 16)
        if (digit == -1) {
            throw RuntimeException("Illegal hexadecimal character $ch at index $index")
        }
        return digit
    }
}