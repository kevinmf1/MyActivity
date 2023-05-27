package com.alcorp.myactivity.tools

import java.text.SimpleDateFormat
import java.util.*

// Helper function to convert date from dd/MM/yyyy format to string format (example: 01/01/2021 to 01 January 2021)
fun dateToString(inputDate: String?): String? {
    val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val inputDateObj = inputDate?.let { inputDateFormat.parse(it) }
    return inputDateObj?.let { outputDateFormat.format(it) }
}