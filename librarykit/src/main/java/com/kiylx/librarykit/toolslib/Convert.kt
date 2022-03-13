package com.kiylx.librarykit.toolslib

fun longToInt(value: Long): Int {
    return value.toInt()
}

fun caclProgress(cur: Long, total: Long): Int {
    if (cur >= 0 && total > 0) {
        return longToInt(cur / total)
    }
    return 0
}