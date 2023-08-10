package com.alcorp.core.domain.usecase

import com.alcorp.core.data.source.local.entity.ProfileEntity
import com.alcorp.core.domain.repository.IProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileInteractor(private val profileRepository: IProfileRepository) : ProfileUseCase {

    override fun getProfile(): Flow<ProfileEntity> = profileRepository.getProfile()

    override fun update(profileData: ProfileEntity) = profileRepository.update(profileData)

    override fun saveImageById(id: Int, image: Int) = profileRepository.saveImageById(id, image)

}