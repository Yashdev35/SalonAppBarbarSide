package com.practicecoding.sallonapp.di

import com.example.sallonappbarbar.data.AuthRepository
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.domain.AuthRepositoryImpl
import com.example.sallonappbarbar.domain.FirestoreDbRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun providesFirestoreRepository(
        repo: FirestoreDbRepositoryImpl
    ): FirestoreRepository
}