package com.martilius.smarthome.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.view.forEach
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.martilius.smarthome.models.Rooms
import com.martilius.smarthome.repository.Repository
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject

class SettingsViewModel @Inject constructor(sharedPreferences: SharedPreferences,repository: Repository):ViewModel(){

    val roomsRespond = MutableLiveData<List<Rooms>>()
    init{
        viewModelScope.launch {
            val findRoomResponse = repository.findRooms()
            if(!findRoomResponse.isNullOrEmpty()){
                roomsRespond.postValue(findRoomResponse)
            }
        }
    }


}