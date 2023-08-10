package com.alcorp.myactivity.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.alcorp.core.data.source.local.entity.ProfileEntity
import com.alcorp.core.domain.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileUseCase: ProfileUseCase) : ViewModel() {

    fun update(profileData: ProfileEntity) = profileUseCase.update(profileData)

    fun saveImageById(id: Int, image: Int) = profileUseCase.saveImageById(id, image)

    val profile = profileUseCase.getProfile().asLiveData()

}