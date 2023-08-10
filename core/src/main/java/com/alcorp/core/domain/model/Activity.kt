package com.alcorp.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Activity(
    var id: Int = 0,
    var date: String? = null,
    var timeStart: String? = null,
    var timeEnd: String? = null,
    var title: String? = null,
    var desc: String? = null,
    var isDone: Boolean = false
) : Parcelable
