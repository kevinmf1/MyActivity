package com.alcorp.core.utils

import java.text.SimpleDateFormat
import java.util.*

// Helper function to get current date as dd/MM/yyyy format
fun getCurrentDate(): String {
    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(currentDate)
}