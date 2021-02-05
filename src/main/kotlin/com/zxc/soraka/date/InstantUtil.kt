package com.zxc.soraka.date

import java.time.Duration
import java.time.Instant

object InstantUtil {

    /**
     * 获取某时刻的范围天数的日期
     * @param instant 时刻
     * @param daysToAdd 范围天数，正数往后计算，负数往前计算
     * @return
     */
    fun getPlusDays(instant: Instant, daysToAdd: Long): Instant {
        return instant.plus(Duration.ofDays(daysToAdd))
    }

    /**
     * 获取某时刻的范围小时的日期
     * @param instant 时刻
     * @param hoursToAdd 范围小时，正数往后计算，负数往前计算
     * @return
     */
    fun getPlusHours(instant: Instant, hoursToAdd: Long): Instant {
        return instant.plus(Duration.ofHours(hoursToAdd))
    }
}