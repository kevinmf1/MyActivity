package com.alcorp.core.domain.usecase

import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.core.domain.repository.IActivityRepository
import kotlinx.coroutines.flow.Flow

class ActivityInteractor(private val activityRepository: IActivityRepository): ActivityUseCase {

    override fun insert(activityList: ActivityEntity) = activityRepository.insert(activityList)

    override fun update(activityList: ActivityEntity) = activityRepository.update(activityList)

    override fun delete(activityList: ActivityEntity) = activityRepository.delete(activityList)

    override fun deleteById(idActivity: Int) = activityRepository.deleteById(idActivity)

    override fun getAllActivity(): Flow<List<ActivityEntity>> = activityRepository.getAllActivity()

    override fun getAllDoneActivity(): Flow<List<ActivityEntity>> = activityRepository.getAllDoneActivity()

    override fun getCountAllActivity(): Flow<Int> = activityRepository.getCountAllActivity()

    override fun getCountActivityToday(): Flow<Int> = activityRepository.getCountActivityToday()

    override fun getCountCompletedActivityToday(): Flow<Int> = activityRepository.getCountCompletedActivityToday()

    override fun getCountUncompletedActivityToday(): Flow<Int> = activityRepository.getCountUncompletedActivityToday()

    override fun getCountUpcomingActivity(): Flow<Int> = activityRepository.getCountUpcomingActivity()

    override fun getCountCompletedUpcomingActivity(): Flow<Int> = activityRepository.getCountCompletedUpcomingActivity()

    override fun getCountUncompletedUpcomingActivity(): Flow<Int> = activityRepository.getCountUncompletedUpcomingActivity()

    override fun getCountLateActivity(): Flow<Int> = activityRepository.getCountLateActivity()

    override fun getCountCompletedActivity(): Flow<Int> = activityRepository.getCountCompletedActivity()

    override fun getActivityToday(): Flow<List<ActivityEntity>> = activityRepository.getActivityToday()

    override fun getUpcomingActivity(): Flow<List<ActivityEntity>> = activityRepository.getUpcomingActivity()

    override fun getLateActivity(): Flow<List<ActivityEntity>> = activityRepository.getLateActivity()

    override fun getCompletedActivity(): Flow<List<ActivityEntity>> =  activityRepository.getCompletedActivity()

}