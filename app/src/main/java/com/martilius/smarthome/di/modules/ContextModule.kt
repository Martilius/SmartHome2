package com.hotLibrary.di.modules

import android.content.Context
import com.martilius.smarthome.SmartHomeApplication
import com.martilius.smarthome.Tasks.ColorPickDialog
import com.martilius.smarthome.Tasks.UdpServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ContextModule {

    @Provides
    fun providesContext(application: SmartHomeApplication): Context = application.applicationContext


}