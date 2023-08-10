package com.alcorp.core.data

import com.alcorp.core.data.source.local.LocalDataSource
import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.core.domain.repository.IActivityRepository
import com.alcorp.core.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IActivityRepository {

    override fun insert(activityList: ActivityEntity) = appExecutors.diskIO().execute { localDataSource.insert(activityList) }

    override fun update(activityList: ActivityEntity) = appExecutors.diskIO().execute { localDataSource.update(activityList) }

    override fun delete(activityList: ActivityEntity) = appExecutors.diskIO().execute { localDataSource.delete(activityList) }

    override fun deleteById(idActivity: Int) = appExecutors.diskIO().execute { localDataSource.deleteById(idActivity) }

    override fun getAllActivity(): Flow<List<ActivityEntity>> = localDataSource.getAllActivity()

    override fun getAllDoneActivity(): Flow<List<ActivityEntity>> = localDataSource.getAllDoneActivity()

    override fun getCountAllActivity(): Flow<Int> = localDataSource.getCountAllActivity()

    override fun getCountActivityToday(): Flow<Int> = localDataSource.getCountActivityToday()

    override fun getCountCompletedActivityToday(): Flow<Int> = localDataSource.getCountCompletedActivityToday()

    override fun getCountUncompletedActivityToday(): Flow<Int> = localDataSource.getCountUncompletedActivityToday()

    override fun getCountUpcomingActivity(): Flow<Int> = localDataSource.getCountUpcomingActivity()

    override fun getCountCompletedUpcomingActivity(): Flow<Int> = localDataSource.getCountCompletedUpcomingActivity()

    override fun getCountUncompletedUpcomingActivity(): Flow<Int> = localDataSource.getCountUncompletedUpcomingActivity()

    override fun getCountLateActivity(): Flow<Int> = localDataSource.getCountLateActivity()

    override fun getCountCompletedActivity(): Flow<Int> = localDataSource.getCountCompletedActivity()

    override fun getActivityToday(): Flow<List<ActivityEntity>> = localDataSource.getActivityToday()

    override fun getUpcomingActivity(): Flow<List<ActivityEntity>> = localDataSource.getUpcomingActivity()

    override fun getLateActivity(): Flow<List<ActivityEntity>> = localDataSource.getLateActivity()

    override fun getCompletedActivity(): Flow<List<ActivityEntity>> = localDataSource.getCompletedActivity()

}