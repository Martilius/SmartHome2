package com.hotLibrary.di.modules

import android.content.Context
import com.martilius.smarthome.SmartHomeApplication
import dagger.Module
import dagger.Provides


@Module
class ContextModule {

    @Provides
    fun providesContext(application: SmartHomeApplication): Context = application.applicationContext
}