package com.alcorp.core.domain.usecase

import com.alcorp.core.data.source.local.entity.ProfileEntity
import dagger.Provides
import kotlinx.coroutines.flow.Flow

interface ProfileUseCase {

    fun getProfile(): Flow<ProfileEntity>

    fun update(profileData: ProfileEntity)

    fun saveImageById(id: Int, image: Int)

}