package com.alcorp.myactivity.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alcorp.core.data.source.local.entity.ActivityEntity
import com.alcorp.core.domain.usecase.ActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val activityUseCase: ActivityUseCase) : ViewModel() {

    fun insert(activity: ActivityEntity) = activityUseCase.insert(activity)

    fun update(activity: ActivityEntity) = activityUseCase.update(activity)

    fun delete(activity: ActivityEntity) = activityUseCase.delete(activity)

    fun deleteById(id: Int) = activityUseCase.deleteById(id)

    val getAllActivity = activityUseCase.getAllActivity().asLiveData()

    val getAllDoneActivity = activityUseCase.getAllDoneActivity().asLiveData()

    val getCountAllActivity = activityUseCase.getCountAllActivity().asLiveData()

    val getCountActivityToday = activityUseCase.getCountActivityToday().asLiveData()

    val getCountCompletedActivityToday = activityUseCase.getCountCompletedActivityToday().asLiveData()

    val getCountUncompletedActivityToday = activityUseCase.getCountUncompletedActivityToday().asLiveData()

    val getCountUpcomingActivity = activityUseCase.getCountUpcomingActivity().asLiveData()

    val getCountCompletedUpcomingActivity = activityUseCase.getCountCompletedUpcomingActivity().asLiveData()

    val getCountUncompletedUpcomingActivity = activityUseCase.getCountUncompletedUpcomingActivity().asLiveData()

    val getCountLateActivity = activityUseCase.getCountLateActivity().asLiveData()

    val getCountCompletedActivity = activityUseCase.getCountCompletedActivity().asLiveData()

    val getActivityToday = activityUseCase.getActivityToday().asLiveData()

    val getUpcomingActivity = activityUseCase.getUpcomingActivity().asLiveData()

    val getLateActivity = activityUseCase.getLateActivity().asLiveData()

    val getCompletedActivity = activityUseCase.getCompletedActivity().asLiveData()
}