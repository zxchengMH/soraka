package com.zxc.soraka.ids

import org.apache.commons.lang3.RandomUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.SystemUtils

import java.net.Inet4Address
import java.net.UnknownHostException

/**
 * Twitter_Snowflake<br></br>
 * SnowFlake的结构如下(每部分用-分开):<br></br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br></br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br></br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br></br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br></br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br></br>
 * 加起来刚好64位，为一个Long型。<br></br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */
class SnowflakeIdWorker(
    /** 工作机器ID(0~31)  */
    private val workerId: Long,
    /** 数据中心ID(0~31)  */
    private val dataCenterId: Long
) {

    // ==============================Fields===========================================
    /** 开始时间截 (2015-01-01)  */
    private val twepoch = 1489111610226L

    /** 机器id所占的位数  */
    private val workerIdBits = 5

    /** 数据标识id所占的位数  */
    private val dataCenterIdBits = 5

    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)  */
    private val maxWorkerId = -1L xor (-1L shl workerIdBits)

    /** 支持的最大数据标识id，结果是31  */
    private val maxDataCenterId = -1L xor (-1L shl dataCenterIdBits)

    /** 序列在id中占的位数  */
    private val sequenceBits = 12

    /** 机器ID向左移12位  */
    private val workerIdShift = sequenceBits

    /** 数据标识id向左移17位(12+5)  */
    private val dataCenterIdShift = sequenceBits + workerIdBits

    /** 时间截向左移22位(5+5+12)  */
    private val timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)  */
    private val sequenceMask = -1 xor (-1 shl sequenceBits)

    /** 毫秒内序列(0~4095)  */
    private var sequence = 0

    /** 上次生成ID的时间截  */
    private var lastTimestamp = -1L

    init {
        if (workerId > maxWorkerId || workerId < 0) {
            throw IllegalArgumentException(
                String.format(
                    "workerId can't be greater than %d or less than 0",
                    maxWorkerId
                )
            )
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw IllegalArgumentException(
                String.format(
                    "dataCenterId can't be greater than %d or less than 0",
                    maxDataCenterId
                )
            )
        }
    }

    // ==============================Methods==========================================
    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    @Synchronized
    fun nextId(): Long {
        var timestamp = timeGen()

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw RuntimeException(
                String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp
                )
            )
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = sequence + 1 and sequenceMask
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp)
            }
        } else {
            sequence = 0
        }//时间戳改变，毫秒内序列重置

        //上次生成ID的时间截
        lastTimestamp = timestamp

        //移位并通过或运算拼到一起组成64位的ID
        return (timestamp - twepoch shl timestampLeftShift.toInt()
                or (dataCenterId shl dataCenterIdShift.toInt())
                or (workerId shl workerIdShift.toInt())
                or sequence.toLong())
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected fun tilNextMillis(lastTimestamp: Long): Long {
        var timestamp = timeGen()
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen()
        }
        return timestamp
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    protected fun timeGen(): Long {
        return System.currentTimeMillis()
    }

    companion object {

        private var idWorker: SnowflakeIdWorker? = null

        init {
            idWorker = SnowflakeIdWorker(workId!!, getDataCenterId())
        }

        private// 如果获取失败，则使用随机数备用
        val workId: Long?
            get() {
                try {
                    val hostAddress = Inet4Address.getLocalHost().hostAddress
                    val ints = StringUtils.toCodePoints(hostAddress)
                    var sums = 0
                    for (b in ints) {
                        sums += b
                    }
                    return (sums % 32).toLong()
                } catch (e: UnknownHostException) {
                    return RandomUtils.nextLong(0, 31)
                }

            }

        private fun getDataCenterId(): Long {
            val ints = StringUtils.toCodePoints(SystemUtils.getHostName()?:"127.0.0.1")
            var sums = 0
            for (i in ints) {
                sums += i
            }
            return (sums % 32).toLong()
        }

        /**
         * 静态工具类
         *
         * @return
         */
        fun generateId(): Long {
            return idWorker!!.nextId()
        }

    }
}

//==============================Test=============================================
/** 测试  */
fun main(args: Array<String>) {
    println(System.currentTimeMillis())
    val startTime = System.nanoTime()
    for (i in 0..49999) {
        val id = SnowflakeIdWorker.generateId()
        println(id.toString() + "  len=>" + id.toString().length)
    }
    println(((System.nanoTime() - startTime) / 1000000).toString() + "ms")
}