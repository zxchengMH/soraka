package com.zxc.soraka.date

import org.apache.commons.lang3.StringUtils
import java.time.*
import java.util.ArrayList
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjusters
import java.time.format.DateTimeFormatter



/**
 * @Auther: zxcheng 1213328214@qq.com
 * @Date: 2019/4/8 20:48
 * @Description: 时间处理工具类
 *  说明：使用1.8新的日期API LocalDate | LocalTime | LocalDateTime封装的工具类
 *  * 之前使用的java.util.Date月份从0开始，我们一般会+1使用，很不方便，java.time.LocalDate月份和星期都改成了enum
 *  * java.util.Date和SimpleDateFormat都不是线程安全的，而LocalDate和LocalTime和最基本的String一样，是不变类型，不但线程安全，而且不能修改。
 *  * java.util.Date是一个“万能接口”，它包含日期、时间，还有毫秒数，更加明确需求取舍
 *  * 新接口更好用的原因是考虑到了日期时间的操作，经常发生往前推或往后推几天的情况。用java.util.Date配合Calendar要写好多代码，
 *  *而且一般的开发人员还不一定能写对。
 */
object DateUtil {

    /**
     * 当前日期格式 DateTimeFormatter线程安全
     */
    private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * 当前时间格式 DateTimeFormatter线程安全
     */
    private val DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


    @JvmStatic
    fun main(args: Array<String>) {
        // 从默认时区的系统时钟获取当前的日期时间。不用考虑时区差
        val date = LocalDateTime.now()
        //2018-07-15T14:22:39.759
        println(date)
        println(getCurrentDate())
        println(getCurrentDateTime())
        // DateTimeFormatter: 格式化时间/日期
        println(dateFormat(getPlusDays(-4)))
        println(getBetweenDates(getLocalDate(), getPlusDays(10)))

        val endDate = "2019-07-0"
        val endDateFormat = DateUtil.dateformat(endDate)
        val flag = DateUtil.compare(endDateFormat, DateUtil.nowDate())
        println("flag$flag")

    }

    /**
     * 获取当前日期 年-月-日
     * @return yyyy-MM-dd
     */
    fun getCurrentDate(): String {
        val date = getLocalDate()
        // 解析字符串to时间
        return DATE_FORMAT.format(date)
    }

    /**
     * 获取当前日期时间 年-月-日 时-分-秒
     * @return yyyy-MM-dd HH:mm:ss
     */
    fun getCurrentDateTime(): String {
        val dateTime = getLocalDateTime()
        // 解析字符串to时间
        return DATE_TIME_FORMAT.format(dateTime)
    }

    /**
     * 获取当前日期
     * @return [LocalDate] 1.8 新的日期对象 2019-04-08
     */
    fun nowDate(): LocalDate {
        return getLocalDate()
    }

    /**
     * 获取当前时间
     * @return [LocalDateTime] 1.8 新的时间对象 2019-04-08T21:26:28.173
     */
    fun nowDateTime(): LocalDateTime {
        return getLocalDateTime()
    }

    /**
     * 字符串转日期时间格式
     * @param date yyyy-MM-dd HH:mm:ss
     * @return [LocalDateTime] 1.8 新的时间对象 2019-04-08T21:26:28.173
     */
    fun dateTimeformat(date: String): LocalDateTime {
        return LocalDateTime.parse(date, DATE_TIME_FORMAT)
    }

    /**
     * 字符串转日期时间格式
     * @param date yyyy-MM-dd
     * @return [LocalDateTime] 1.8 新的时间对象 2019-04-08T21:26:28.173
     */
    fun dateformat(date: String): LocalDate {
        return LocalDate.parse(date, DATE_FORMAT)
    }

    /**
     * 日期格式转字符串
     * @param date [LocalDate] 1.8 新的时间对象 2019-04-08
     * @return yyyy-MM-dd
     */
    fun dateFormat(date: LocalDate): String {
        return DATE_FORMAT.format(date)
    }

    /**
     * 日期时间格式转字符串
     * @param date [LocalDateTime] 1.8 新的时间对象 2019-04-08T21:26:28.173
     * @return yyyy-MM-dd HH:mm:ss
     */
    fun dateTimeformat(date: LocalDateTime): String {
        return DATE_TIME_FORMAT.format(date)
    }

    /**
     * 获取本月第一天
     * @return [LocalDate] 1.8 新的日期对象 2019-04-08
     */
    fun firstDayOfMonth(): LocalDate {
        val today = getLocalDate()
        // 取本月第1天： 2018-05-01
        return today.with(TemporalAdjusters.firstDayOfMonth())
    }

    /**
     * 获取本月最后一天
     * @return [LocalDate] 1.8 新的日期对象 2019-04-08
     */
    fun lastDayOfMonth(): LocalDate {
        val today = getLocalDate()
        // 取本月第1天： 2018-05-01
        return today.with(TemporalAdjusters.lastDayOfMonth())
    }

    /**
     * 获取当前日期的范围天数的日期
     * @param daysToAdd 范围天数，正数往后计算，负数往前计算
     * @return
     */
    fun getPlusDays(daysToAdd: Long): LocalDate {
        val currentDate = getLocalDate()
        return currentDate.plusDays(daysToAdd)
    }

    /**
     * 获取当前日期的范围周数的日期
     * @param weeksToAdd 范围周数，正数往后计算，负数往前计算
     * @return
     */
    fun getPlusWeeks(weeksToAdd: Long): LocalDate {
        val currentDate = getLocalDate()
        return currentDate.plusWeeks(weeksToAdd)
    }

    /**
     * 获取当前日期的某个范围月数的日期
     * @param monthsToAdd 范围月数，正数往后计算，负数往前计算
     * @return
     */
    fun getPlusMonths(monthsToAdd: Long): LocalDate {
        val currentDate = getLocalDate()
        return currentDate.plusMonths(monthsToAdd)
    }

    /**
     * 获取当前日期的某个范围年数的日期
     * @param yearsToAdd 范围年数，正数往后计算，负数往前计算
     * @return
     */
    fun getPlusYears(yearsToAdd: Long): LocalDate {
        val currentDate = getLocalDate()
        return currentDate.plusYears(yearsToAdd)
    }

    /**
     * 计算两个时间之间的间隔毫秒时间
     * @return
     */
    fun timesBetween(startInclusive: Temporal, endExclusive: Temporal): Long {
        val duration = Duration.between(startInclusive, endExclusive)
        return duration.toMillis()
    }

    /**
     * 计算两个日期之间的间隔天数
     * @return
     */
    fun daysBetween(startDateInclusive: LocalDate, endDateExclusive: LocalDate): Int {
        val period = Period.between(startDateInclusive, startDateInclusive)
        return period.days
    }

    /**
     * 计算两个日期之间的间隔月份数
     * @return
     */
    fun monthsBetween(startDateInclusive: LocalDate, endDateExclusive: LocalDate): Int {
        val period = Period.between(startDateInclusive, startDateInclusive)
        return period.months
    }

    /**
     * 计算两个日期之间的间隔年数
     * @return
     */
    fun yearsBetween(startDateInclusive: LocalDate, endDateExclusive: LocalDate): Int {
        val period = Period.between(startDateInclusive, startDateInclusive)
        return period.years
    }

    /**
     * 计算两个日期之间的日期
     * <pre>
     * LocalDate a = LocalDate.of(2012, 6, 30);
     * LocalDate b = LocalDate.of(2012, 7, 1);
     * a.isAfter(b) == false
     * a.isAfter(a) == false
     * b.isAfter(a) == true
     * </pre>
     * @return
     */
    fun getBetweenDates(startDateInclusive: LocalDate, endDateExclusive: LocalDate): List<LocalDate> {

        val list = ArrayList<LocalDate>()
        var postDate = startDateInclusive
        while (endDateExclusive.isAfter(postDate)) {
            list.add(postDate)
            postDate = postDate.plusDays(1)
        }
        list.add(endDateExclusive)
        return list
    }

    /**
     * 比较两个日期的大小
     * @param date1
     * @param date2
     * @return
     */
    fun compare(date1: LocalDate, date2: LocalDate): Int {
        return date1.compareTo(date2)
    }

    /**
     * 比较两个日期的大小
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    fun compare(dateTime1: LocalDateTime, dateTime2: LocalDateTime): Int {
        return dateTime1.compareTo(dateTime2)
    }

    /**
     * 获取本地日期
     * 从默认时区的系统时钟获取当前的日期时间。不用考虑时区差
     * @return [LocalDate] 1.8 新的日期对象 2019-04-08
     */
    private fun getLocalDate(): LocalDate {
        return LocalDate.now()
    }

    /**
     * 获取本地时间
     * 从默认时区的系统时钟获取当前的日期时间。不用考虑时区差
     * @return [LocalDateTime] 1.8 新的时间对象 2019-04-08T21:26:28.173
     */
    private fun getLocalDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }
}

fun main() {
    val dataFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
    println(DatePatternEnum.DATE_TIME_MS_PATTERN.format(today_start))
    println(DatePatternEnum.DATE_PATTERN.format(today_start))
    println(DatePatternEnum.DATE_TIME_PATTERN.format(today_start))

    val today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
    val end = today_end.format(dataFormat)
    println(end)
    println(StringUtils.join(listOf(1,2,3), ","))
}