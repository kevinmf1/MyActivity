package com.alcorp.myactivity.database.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "gender")
    var gender: String? = null,

    @ColumnInfo(name = "date_birth")
    var dateBirth: String? = null,

    @ColumnInfo(name = "phone")
    var phone: String? = null,

    @ColumnInfo(name = "address")
    var address: String? = null,

    @ColumnInfo(name = "photo")
    var photo: Int? = null
)