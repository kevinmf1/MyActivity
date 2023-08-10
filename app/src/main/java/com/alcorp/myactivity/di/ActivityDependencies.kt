package com.alcorp.myactivity.di

import com.alcorp.core.domain.usecase.ActivityUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ActivityDependencies {

    fun activityUseCase(): ActivityUseCase

}