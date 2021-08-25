package com.example.ownerapp.di.modules

import android.content.Context
import com.example.ownerapp.mvvm.repository.AuthRepository
import com.example.ownerapp.mvvm.repository.BaseRepository
import com.example.ownerapp.mvvm.repository.MainRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule constructor(private var context: Context) {


    @Provides
    fun provideMainRepository(): BaseRepository {
        return MainRepository(contextMain = context)
    }

    @Provides
    fun provideAuthRepository(): BaseRepository {
        return AuthRepository(context = context)
    }

}