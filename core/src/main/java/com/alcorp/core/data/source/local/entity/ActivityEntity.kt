package com.alcorp.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_entity")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "timeStart")
    var timeStart: String? = null,

    @ColumnInfo(name = "timeEnd")
    var timeEnd: String? = null,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "desc")
    var desc: String? = null,

    @ColumnInfo(name = "isDone")
    var isDone: Boolean = false
)
