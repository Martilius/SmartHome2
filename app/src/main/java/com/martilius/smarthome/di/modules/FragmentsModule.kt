package com.martilius.smarthome.di.modules

import com.martilius.smarthome.Tasks.ColorPickDialog
import com.martilius.smarthome.adapters.LedAdapter
import com.martilius.smarthome.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class FragmentsModule {

    @ContributesAndroidInjector
    internal abstract fun bindLoginFragment(): LoginFragment


    @ContributesAndroidInjector
    internal abstract fun bindPawelsRoomFragment(): PawelsRoomFragment


    @ContributesAndroidInjector
    internal abstract fun bindSettingsFragment(): SettingsFragment



}