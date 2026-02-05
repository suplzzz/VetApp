package com.suplz.vetapp.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    private val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    fun formatCurrentDate(): String {
        return formatter.format(Date())
    }

    fun formatDateToString(timestamp: Long): String {
        val date = Date(timestamp)
        return formatter.format(date)
    }
}