package com.martilius.smarthome.di.modules

import androidx.lifecycle.MutableLiveData
import com.martilius.smarthome.repository.Repository
import com.martilius.smarthome.repository.remote.ConfigurationService
import com.martilius.smarthome.repository.remote.UserService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
            configurationService: ConfigurationService, userService: UserService
    ) = Repository(configurationService, userService)
}