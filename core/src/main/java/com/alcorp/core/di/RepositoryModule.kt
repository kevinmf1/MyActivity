package com.alcorp.core.di

import com.alcorp.core.data.ActivityRepository
import com.alcorp.core.data.ProfileRepository
import com.alcorp.core.domain.repository.IActivityRepository
import com.alcorp.core.domain.repository.IProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(activityRepository: ActivityRepository): IActivityRepository

    @Binds
    abstract fun provideProfileRepository(profileRepository: ProfileRepository): IProfileRepository
}