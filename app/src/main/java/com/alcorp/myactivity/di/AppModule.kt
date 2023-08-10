package com.alcorp.myactivity.di

import com.alcorp.core.data.ActivityRepository
import com.alcorp.core.data.ProfileRepository
import com.alcorp.core.domain.usecase.ActivityInteractor
import com.alcorp.core.domain.usecase.ActivityUseCase
import com.alcorp.core.domain.usecase.ProfileInteractor
import com.alcorp.core.domain.usecase.ProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsUseCase(activityRepository: ActivityRepository): ActivityUseCase=ActivityInteractor(activityRepository)

    @Provides
    @Singleton
    fun provideProfileUseCase(profileRepository: ProfileRepository): ProfileUseCase=ProfileInteractor(profileRepository)
}