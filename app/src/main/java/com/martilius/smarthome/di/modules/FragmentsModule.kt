package com.hotLibrary.di.modules

import com.martilius.smarthome.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class FragmentsModule {

    @ContributesAndroidInjector
    internal abstract fun bindLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    internal abstract fun bindOutsideFragment(): OutsideFragment



    @ContributesAndroidInjector
    internal abstract fun bindPawelsRoomFragment(): PawelsRoomFragment

    @ContributesAndroidInjector
    internal abstract fun bindSaloonFragment(): SaloonFragment

    @ContributesAndroidInjector
    internal abstract fun bindSettingsFragment(): SettingsFragment

}