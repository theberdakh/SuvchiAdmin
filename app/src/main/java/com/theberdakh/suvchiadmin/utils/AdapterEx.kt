package com.theberdakh.suvchiadmin.utils

import java.text.SimpleDateFormat
import java.util.Locale


 fun convertDateTime(time: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val parsedDate = inputFormat.parse(time)
    val outputFormat = SimpleDateFormat("HH:mm, dd-MM-yyyy", Locale.getDefault())
    return outputFormat.format(parsedDate)
}
