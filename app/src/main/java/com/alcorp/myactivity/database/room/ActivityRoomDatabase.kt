package com.alcorp.myactivity.database.room

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alcorp.myactivity.R
import com.alcorp.myactivity.database.repository.ActivityDao
import com.alcorp.myactivity.database.repository.ActivityEntity
import com.alcorp.myactivity.database.repository.ProfileDao
import com.alcorp.myactivity.database.repository.ProfileEntity
import com.alcorp.myactivity.tools.MigrationValue

@Database(entities = [ActivityEntity::class, ProfileEntity::class], version = 3)
abstract class ActivityRoomDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun profileDao(): ProfileDao

    companion object {
        private const val DATABASE_NAME = "activity_database.db"

        @Volatile
        private var INSTANCE: ActivityRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ActivityRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): ActivityRoomDatabase {
            return Room.databaseBuilder(context, ActivityRoomDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        insertDefaultProfile(db)
                    }
                })
                .addMigrations(MigrationValue())
                .build()
        }

        private fun insertDefaultProfile(db: SupportSQLiteDatabase) {
            val defaultProfile = ProfileEntity(
                name = "Users",
                gender = "Man",
                dateBirth = "5-3-2003",
                phone = "081234567890",
                address = "Jl. Raya Telang, Kecamatan Kamal, Kabupaten Bangkalan, Provinsi Jawa Timur",
                photo = R.drawable.man_pic1
            )

            val contentValues = ContentValues().apply {
                put("name", defaultProfile.name)
                put("gender", defaultProfile.gender)
                put("date_birth", defaultProfile.dateBirth)
                put("phone", defaultProfile.phone)
                put("address", defaultProfile.address)
                put("photo", defaultProfile.photo)
            }

            db.insert("profile", SQLiteDatabase.CONFLICT_REPLACE, contentValues)
        }
    }
}