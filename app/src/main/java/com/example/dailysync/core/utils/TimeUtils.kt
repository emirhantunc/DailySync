package com.example.dailysync.core.utils

import android.content.Context
import com.example.dailysync.R

fun getTimeAgo(timestamp: Long, context: Context): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val seconds = diff / 1000
    val minutes = (seconds / 60).toInt()
    val hours = (minutes / 60)
    val days = (hours / 24)
    val weeks = (days / 7)
    val months = (days / 30)
    val years = (days / 365)

    return when {
        years > 0 -> context.resources.getQuantityString(R.plurals.years_ago, years, years)
        months > 0 -> context.resources.getQuantityString(
            R.plurals.months_ago,
            months,
            months
        )

        weeks > 0 -> context.resources.getQuantityString(R.plurals.weeks_ago, weeks, weeks)
        days > 0 -> context.resources.getQuantityString(R.plurals.days_ago, days, days)
        hours > 0 -> context.resources.getQuantityString(R.plurals.hours_ago, hours, hours)
        minutes > 0 -> context.resources.getQuantityString(
            R.plurals.minutes_ago,
            minutes,
            minutes
        )

        else -> context.getString(R.string.just_now)
    }
}

