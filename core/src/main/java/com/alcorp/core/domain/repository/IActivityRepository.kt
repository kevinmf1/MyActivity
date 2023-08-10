package com.alcorp.core.domain.repository

import com.alcorp.core.data.source.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

interface IActivityRepository {

    fun insert(activityList: ActivityEntity)

    fun update(activityList: ActivityEntity)

    fun delete(activityList: ActivityEntity)

    fun deleteById(idActivity: Int)

    fun getAllActivity(): Flow<List<ActivityEntity>>

    fun getAllDoneActivity(): Flow<List<ActivityEntity>>

    fun getCountAllActivity(): Flow<Int>

    fun getCountActivityToday(): Flow<Int>

    fun getCountCompletedActivityToday(): Flow<Int>

    fun getCountUncompletedActivityToday(): Flow<Int>

    fun getCountUpcomingActivity(): Flow<Int>

    fun getCountCompletedUpcomingActivity(): Flow<Int>

    fun getCountUncompletedUpcomingActivity(): Flow<Int>

    fun getCountLateActivity(): Flow<Int>

    fun getCountCompletedActivity(): Flow<Int>

    fun getActivityToday(): Flow<List<ActivityEntity>>

    fun getUpcomingActivity(): Flow<List<ActivityEntity>>

    fun getLateActivity(): Flow<List<ActivityEntity>>

    fun getCompletedActivity(): Flow<List<ActivityEntity>>
}