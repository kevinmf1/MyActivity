package com.alcorp.core.domain.repository

import com.alcorp.core.data.source.local.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow

interface IProfileRepository {

    fun getProfile(): Flow<ProfileEntity>

    fun update(profileData: ProfileEntity)

    fun saveImageById(id: Int, image: Int)
}