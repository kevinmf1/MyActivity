package com.alcorp.core.di

import android.content.ContentValues
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alcorp.core.R
import com.alcorp.core.data.source.local.entity.ProfileEntity
import com.alcorp.core.data.source.local.room.ActivityDatabase
import com.alcorp.core.utils.MigrationValue
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ActivityDatabase {
//        val passphrase: ByteArray = SQLiteDatabase.getBytes("myactivity".toCharArray())
//        val factory = SupportFactory(passphrase)
        return Room.databaseBuilder(
            context,
            ActivityDatabase::class.java, "Activity.db"
        )
            .addCallback(AppDatabaseCallback)
//            .fallbackToDestructiveMigration()
            .addMigrations(MigrationValue())
//            .openHelperFactory(factory)
            .build()
    }

    @Provides
    fun provideActivityDao(database: ActivityDatabase) = database.activityDao()

    @Provides
    fun provideProfileDao(database: ActivityDatabase) = database.profileDao()
}

object AppDatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        insertDefaultProfile(db)
    }
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