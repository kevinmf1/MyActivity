package com.alcorp.myactivity.database.repository

import android.app.Application
import com.alcorp.myactivity.database.room.ActivityRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ProfileRepository(application: Application) {
    private val mProfileDao: ProfileDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ActivityRoomDatabase.getDatabase(application)
        mProfileDao = db.profileDao()
    }

    fun getProfile() = mProfileDao.getProfile()

    fun update(profileData: ProfileEntity) {
        executorService.execute { mProfileDao.update(profileData) }
    }

    fun saveImageById(id: Int, image: Int) {
        executorService.execute { mProfileDao.saveImageById(id, image) }
    }
}