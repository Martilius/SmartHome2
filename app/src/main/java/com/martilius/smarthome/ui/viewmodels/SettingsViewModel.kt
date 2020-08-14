package com.martilius.smarthome.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.martilius.smarthome.models.DeviceSettings
import com.martilius.smarthome.models.Rooms
import com.martilius.smarthome.models.UserSettingsRespond
import com.martilius.smarthome.repository.Repository
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject

class SettingsViewModel @Inject constructor(sharedPreferences: SharedPreferences,repository: Repository):ViewModel(){

    val roomsRespond = MutableLiveData<List<Rooms>>()
    val usersRespond = MutableLiveData<List<UserSettingsRespond>>()
    val usersListChanged = MutableLiveData<List<UserSettingsRespond>>()
    val devicesRespond = MutableLiveData<MutableList<DeviceSettings>>()
    init{
        viewModelScope.launch {
            val findRoomResponse = repository.findRooms()
            if(!findRoomResponse.isNullOrEmpty()){
                roomsRespond.postValue(findRoomResponse)
            }
            val findUsers = repository.getOnlyUsers()
            if(!findUsers.isNullOrEmpty()){
                usersRespond.postValue(findUsers)
            }
            val findDevices = repository.getAllDevices()
            val deviceSettings: MutableList<DeviceSettings> = mutableListOf()
            if(!findDevices.isNullOrEmpty()){
                findDevices.forEach {
                    deviceSettings.add(DeviceSettings(it.id,it.name,it.room, null))
                }
            }
            devicesRespond.postValue(deviceSettings)
        }
    }

    fun subscribeUsers(stompClient:StompClient, destination:String){
        stompClient.topic(destination)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
                val arrayType = object : TypeToken<List<UserSettingsRespond>>() {}.type
                val received: List<UserSettingsRespond> = Gson().fromJson(it.payload, arrayType)
                //postUsers(received, list)
                //  viewModel.receive(it.payload.toString())
                //Toast.makeText(context, it.payload.toString(), Toast.LENGTH_LONG).show()

                usersListChanged.postValue(received)
            }, { t: Throwable? ->
                //  viewModel.receive(t.toString())
            })
    }

    fun postUsers(users:List<UserSettingsRespond>, list:MutableList<UserSettingsRespond>){
        var adapterList = mutableListOf<String>()
        var usersList = mutableListOf<String>()
        list.forEach {
            adapterList.add(it.login.toString())
        }
        users.forEach {
            usersList.add(it.login.toString())
        }
        if(!checkIfTheSame(adapterList,usersList)){
            usersRespond.postValue(users)
        }
    }

    fun checkIfTheSame(list1: MutableList<String>, list2: MutableList<String>): Boolean {

        return list1.containsAll(list2) && list2.containsAll(list1)
    }

    @SuppressLint("CheckResult")
    @InternalCoroutinesApi
    fun connection(stompClient: StompClient, destination: String, context:Context) {

        stompClient.withServerHeartbeat(10000)
        subscribeUsers(stompClient, destination)
        stompClient.connect()
        stompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
                when (it.type) {
                    LifecycleEvent.Type.OPENED -> {
                        // viewModel.receive(it.type.name)
                    }
                    LifecycleEvent.Type.ERROR -> {
                        //check(it.message.toString())
                        //  viewModel.receive(it.type.name)
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        val connectivityManager =
                            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        val networkRequest = NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                            .build()
                        var networkCallback = object : ConnectivityManager.NetworkCallback() {
                            override fun onAvailable(network: Network) {
                                subscribeUsers(stompClient,destination)
                                stompClient.connect()
                                connectivityManager.unregisterNetworkCallback(this)
                            }

                        }


                        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
                    }
                }
            }, { t: Throwable ->
                // viewModel.receive(t.toString())
            })


    }

}