package com.kiylx.toolslib

fun longToInt(value: Long): Int {
    return value.toInt()
}

/**
 * 把每秒下载多少byte转换成易于阅读的格式
 */

fun convertSpeed(value: Long): String {

}

fun caclProgress(cur: Long, total: Long): Int {
    if (cur >= 0 && total > 0) {
        return  longToInt(cur/total)
    }
    return 0
}