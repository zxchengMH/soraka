package com.zxc.soraka.date

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.HashMap

enum class DatePatternEnum(private var index: Int,
                           private var pattern: String,
                           private var comment: String) {

    DATE_TIME_MS_PATTERN(0, "yyyy-MM-dd HH:mm:ss.SSS", "年-月-日 时:分:秒.毫秒"),

    DATE_TIME_PATTERN(1, "yyyy-MM-dd HH:mm:ss", "年-月-日 时:分:秒"),

    TIME_PATTERN(2, "HH:mm:ss", "时:分:秒"),

    MINUTE_PATTERN(3, "yyyy-MM-dd HH:mm", "年-月-日 时:分"),

    DATE_PATTERN(4, "yyyy-MM-dd", "年-月-日"),

    MONTH_PATTERN(5, "yyyy-MM", "年-月"),

    ONLY_YEAR_PATTERN(6, "yyyy", "年"),

    ONLY_MONTH_PATTERN(7, "MM", "月"),

    ONLY_DAY_PATTERN(8, "dd", "日"),

    ONLY_HOUR_PATTERN(9, "HH", "时"),

    ONLY_MINUTE_PATTERN(10, "mm", "分"),

    ONLY_SECOND_PATTERN(11, "ss", "秒"),

    ZN_DATE_TIME_MS_PATTERN(12, "yyyy年MM月dd日 HH时mm分ss秒SSS毫秒", "中文格式年月日时分秒毫秒"),

    ZN_DATE_TIME_PATTERN(13, "yyyy年MM月dd日 HH时mm分ss秒", "中文格式年月日时分秒"),

    ZN_DATE_PATTERN(14, "yyyy年MM月dd日", "中文格式年月日"),

    ZN_MONTH_PATTERN(15, "yyyy年MM月", "中文格式年月"),

    ZN_YEAR_ONLY_PATTERN(16, "yyyy年", "中文格式年"),

    ZN_TIME_PATTERN(17, "HH时mm分ss秒", "中文格式时分秒"),

    GAP_LESS_DATE_TIME_PATTERN(18, "yyyyMMddHHmmss", "无间隔符的年月日时分秒"),

    GAP_LESS_DATE_TIME_MS_PATTERN(19, "yyyyMMddHHmmssSSS", "无间隔符的年月日时分秒毫秒"),

    GAP_LESS_DATE_PATTERN(20, "yyyyMMdd", "无间隔符的年月日");

    fun getIndex(): Int{
        return index
    }

    fun getPattern(): String{
        return pattern
    }

    private val formatterCache = HashMap<DatePatternEnum, DateTimeFormatter>()

    private fun checkCache(){

        if(formatterCache.isEmpty()){
            formatterCache.clear()
            values().forEach { e ->
                formatterCache[e] = DateTimeFormatter.ofPattern(e.pattern)
            }
        }
    }

    fun format(dateTime: LocalDateTime): String?{
        checkCache()
        return formatterCache[this]?.format(dateTime)
    }
}