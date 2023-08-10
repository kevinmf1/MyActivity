package com.alcorp.core.data.source.local

import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.core.data.source.local.entity.ProfileEntity
import com.alcorp.core.data.source.local.room.ActivityDao
import com.alcorp.core.data.source.local.room.ProfileDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val activityDao: ActivityDao, private val profileDao: ProfileDao) {

    fun update(profileData: ProfileEntity) = profileDao.update(profileData)

    fun getProfile(): Flow<ProfileEntity> = profileDao.getProfile()

    fun saveImageById(id: Int, image: Int) = profileDao.saveImageById(id, image)

    fun insert(activityList: ActivityEntity) = activityDao.insert(activityList)

    fun update(activityList: ActivityEntity) = activityDao.update(activityList)

    fun delete(activityList: ActivityEntity) = activityDao.delete(activityList)

    fun deleteById(idActivity: Int) = activityDao.deleteById(idActivity)

    fun getAllActivity(): Flow<List<ActivityEntity>> = activityDao.getAllActivity()

    fun getAllDoneActivity(): Flow<List<ActivityEntity>> = activityDao.getAllDoneActivity()

    fun getCountAllActivity(): Flow<Int> = activityDao.getCountAllActivity()

    fun getCountActivityToday(): Flow<Int> = activityDao.getCountActivityToday()

    fun getCountCompletedActivityToday(): Flow<Int> = activityDao.getCountCompletedActivityToday()

    fun getCountUncompletedActivityToday(): Flow<Int> = activityDao.getCountUncompletedActivityToday()

    fun getCountUpcomingActivity(): Flow<Int> = activityDao.getCountUpcomingActivity()

    fun getCountCompletedUpcomingActivity(): Flow<Int> = activityDao.getCountCompletedUpcomingActivity()

    fun getCountUncompletedUpcomingActivity(): Flow<Int> = activityDao.getCountUncompletedUpcomingActivity()

    fun getCountLateActivity(): Flow<Int> = activityDao.getCountLateActivity()

    fun getCountCompletedActivity(): Flow<Int> = activityDao.getCountCompletedActivity()

    fun getActivityToday(): Flow<List<ActivityEntity>> = activityDao.getActivityToday()

    fun getUpcomingActivity(): Flow<List<ActivityEntity>> = activityDao.getUpcomingActivity()

    fun getLateActivity(): Flow<List<ActivityEntity>> = activityDao.getLateActivity()

    fun getCompletedActivity(): Flow<List<ActivityEntity>> = activityDao.getCompletedActivity()

}