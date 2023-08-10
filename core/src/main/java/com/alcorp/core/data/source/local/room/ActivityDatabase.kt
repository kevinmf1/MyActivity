package com.alcorp.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.core.data.source.local.entity.ProfileEntity

@Database(entities = [ActivityEntity::class, ProfileEntity::class], version = 1, exportSchema = false)
abstract class ActivityDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao

    abstract fun profileDao(): ProfileDao

}