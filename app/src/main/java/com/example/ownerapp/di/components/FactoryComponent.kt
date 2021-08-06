package com.example.ownerapp.di.components

import com.example.ownerapp.di.modules.FactoryModule
import com.example.ownerapp.di.modules.RepositoryModule
import com.example.ownerapp.mvvm.factory.ModelFactory
import dagger.Component

@Component(modules = [FactoryModule::class, RepositoryModule::class])
interface FactoryComponent {

    fun getFactory(): ModelFactory
}