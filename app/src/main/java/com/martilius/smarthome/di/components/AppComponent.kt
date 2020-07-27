package com.martilius.smarthome.di.components


import com.hotLibrary.di.modules.*

import com.hotLibrary.di.modules.FragmentsModule
import com.martilius.smarthome.SmartHomeApplication
import com.martilius.smarthome.di.modules.ActivityModule
import com.martilius.smarthome.di.modules.RemoteModule
import com.martilius.smarthome.di.modules.RepositoryModule
import com.martilius.smarthome.di.modules.SharedPrefsModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            FragmentsModule::class,
            ViewModelModule::class,
            ActivityModule::class,
            ContextModule::class,
            ViewModelFactoryModule::class,
            SharedPrefsModule::class,
            RemoteModule::class,
            RepositoryModule::class
        ]
)

interface AppComponent : AndroidInjector<SmartHomeApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<SmartHomeApplication>()
}