package com.example.ownerapp.di.modules

import com.example.ownerapp.mvvm.factory.ModelFactory
import com.example.ownerapp.mvvm.repository.BaseRepository
import dagger.Module
import dagger.Provides

@Module
class FactoryModule constructor(private var mRepository: BaseRepository) {

    @Provides
    fun provideModalFactory(): ModelFactory {
        return ModelFactory(mRepository)
    }

    @Provides
    fun providesRepository(): BaseRepository {
        return mRepository
    }
}