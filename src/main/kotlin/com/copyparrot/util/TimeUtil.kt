package com.copyparrot.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeUtil {

    companion object {
        fun getCurrentTimeAsString(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return current.format(formatter)
        }
    }

}