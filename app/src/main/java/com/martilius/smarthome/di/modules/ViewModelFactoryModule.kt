package com.martilius.smarthome.di.modules

import androidx.lifecycle.ViewModelProvider
import com.martilius.smarthome.di.modules.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}