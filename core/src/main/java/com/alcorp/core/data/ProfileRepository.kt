package com.alcorp.core.data

import com.alcorp.core.data.source.local.LocalDataSource
import com.alcorp.core.data.source.local.entity.ProfileEntity
import com.alcorp.core.domain.repository.IProfileRepository
import com.alcorp.core.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IProfileRepository {

    override fun getProfile(): Flow<ProfileEntity> = localDataSource.getProfile()

    override fun update(profileData: ProfileEntity) = appExecutors.diskIO().execute { localDataSource.update(profileData) }

    override fun saveImageById(id: Int, image: Int) = appExecutors.diskIO().execute { localDataSource.saveImageById(id, image) }
}