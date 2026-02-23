package com.example.dailysync.features.home.presentation.helpers

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy, EEEE", Locale("en"))
    return dateFormat.format(Date())
}
