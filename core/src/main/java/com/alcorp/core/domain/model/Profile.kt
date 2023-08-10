package com.alcorp.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    var id: Int = 0,
    var name: String? = null,
    var gender: String? = null,
    var dateBirth: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var photo: Int? = null
) : Parcelable
