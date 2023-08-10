package com.alcorp.myactivity.di

import com.alcorp.core.domain.usecase.ProfileUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProfileDependencies {

    fun profileUseCase(): ProfileUseCase

}