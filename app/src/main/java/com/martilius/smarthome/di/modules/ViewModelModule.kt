package com.martilius.smarthome.di.modules

import androidx.lifecycle.ViewModel
import com.martilius.smarthome.di.modules.ViewModelKey
import com.martilius.smarthome.ui.viewmodels.*

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(key = LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(key = OutsideViewModel::class)
    abstract fun bindOutsideViewModel(viewModel: OutsideViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(key = PawelsRoomViewModel::class)
    abstract fun bindPawelsRoomViewModel(viewModel: PawelsRoomViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(key = SaloonViewModel::class)
    abstract fun bindSaloonViewModel(viewModel: SaloonViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(key= SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}