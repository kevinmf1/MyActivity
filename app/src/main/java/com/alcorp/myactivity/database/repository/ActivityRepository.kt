package com.alcorp.myactivity.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.alcorp.myactivity.database.room.ActivityRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ActivityRepository(application: Application) {
    private val mActivityDao: ActivityDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ActivityRoomDatabase.getDatabase(application)
        mActivityDao = db.activityDao()
    }

    fun getAllActivity(): LiveData<List<ActivityEntity>> = mActivityDao.getAllActivity()

    fun insert(activity: ActivityEntity) {
        executorService.execute { mActivityDao.insert(activity) }
    }

    fun deleteActivityById(activityId: Int) {
        mActivityDao.deleteById(activityId)
    }

    fun update(activity: ActivityEntity) {
        executorService.execute { mActivityDao.update(activity) }
    }

    fun getAllDoneActivity(): LiveData<List<ActivityEntity>> = mActivityDao.getAllDoneActivity()

    fun getCountAllActivity(): LiveData<Int> = mActivityDao.getCountAllActivity()

    fun getCountActivityToday(): LiveData<Int> = mActivityDao.getCountActivityToday()

    fun getCountCompletedActivityToday(): LiveData<Int> = mActivityDao.getCountCompletedActivityToday()

    fun getCountUncompletedActivityToday(): LiveData<Int> = mActivityDao.getCountUncompletedActivityToday()

    fun getCountUpcomingActivity(): LiveData<Int> = mActivityDao.getCountUpcomingActivity()

    fun getCountCompletedUpcomingActivity(): LiveData<Int> = mActivityDao.getCountCompletedUpcomingActivity()

    fun getCountUncompletedUpcomingActivity(): LiveData<Int> = mActivityDao.getCountUncompletedUpcomingActivity()

    fun getCountLateActivity(): LiveData<Int> = mActivityDao.getCountLateActivity()

    fun getCountCompletedActivity(): LiveData<Int> = mActivityDao.getCountCompletedActivity()

    fun getActivityToday(): LiveData<List<ActivityEntity>> = mActivityDao.getActivityToday()

    fun getUpcomingActivity(): LiveData<List<ActivityEntity>> = mActivityDao.getUpcomingActivity()

    fun getLateActivity(): LiveData<List<ActivityEntity>> = mActivityDao.getLateActivity()

    fun getCompletedActivity(): LiveData<List<ActivityEntity>> = mActivityDao.getCompletedActivity()
}
