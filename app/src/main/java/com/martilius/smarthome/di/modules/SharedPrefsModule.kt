package com.martilius.smarthome.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefsModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences("hot_library", Context.MODE_PRIVATE)
}