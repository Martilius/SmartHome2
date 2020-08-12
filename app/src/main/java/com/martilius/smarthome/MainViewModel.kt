package com.martilius.smarthome

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.media.browse.MediaBrowser
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.Toast
import androidx.core.view.forEach
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.martilius.smarthome.adapters.NewDeviceAdapter
import com.martilius.smarthome.models.DeviceType
import com.martilius.smarthome.models.NewDevice
import com.martilius.smarthome.models.RoomModelRespond
import com.martilius.smarthome.models.Rooms
import com.martilius.smarthome.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adding_devices_dialog.view.*
import kotlinx.android.synthetic.main.room_adding_dialog.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject

class MainViewModel @Inject constructor(sharedPreferences: SharedPreferences, private val repository: Repository) : ViewModel() {

    val menuList = MutableLiveData<List<Rooms>>()
    val newTitle=MutableLiveData<String>()
    val roomCountChanged = MutableLiveData<List<Rooms>>()
    val deviceTypeResponse = MutableLiveData<List<DeviceType>>()
    val checkResponse = MutableLiveData<String>()
    val menuListNull = MutableLiveData<Boolean>()
    val deviceIp = MutableLiveData<String>()


    init {
        viewModelScope.launch {
            val findRoomsRespond = repository.findRooms()
            if(!findRoomsRespond.isNullOrEmpty()){
                menuList.postValue(findRoomsRespond)
            }else{
                menuListNull.postValue(true)
            }
            val findDeviceTypeRespond = repository.findDeviceType()
            if(!findDeviceTypeRespond.isNullOrEmpty()){
                deviceTypeResponse.postValue(findDeviceTypeRespond)
            }
        }
    }

    fun changeTitle(title:String){
        newTitle.value = title
    }


    fun check(bla:String){
        checkResponse.postValue(bla)
    }

    fun postRooms(rooms:List<Rooms>, navView: NavigationView){
        var navMenuList = mutableListOf<String>()
        var roomsList = mutableListOf<String>()
        navView.menu.forEach {
            navMenuList.add(it.title.toString())
        }
        navMenuList.remove(navMenuList.last())
        rooms.forEach {
            roomsList.add(it.roomName)
        }
        if(!checkIfTheSame(navMenuList,roomsList)){
            roomCountChanged.postValue(rooms)
        }

        roomCountChanged.postValue(rooms)
    }

    fun checkIfTheSame(list1: MutableList<String>, list2: MutableList<String>): Boolean {

        return list1.containsAll(list2) && list2.containsAll(list1)
    }

    @SuppressLint("CheckResult")
    @InternalCoroutinesApi
    fun connection(stompClient: StompClient, navView: NavigationView, context:Context) {

        stompClient.withServerHeartbeat(10000)
        subscribeRoomsChange(stompClient,navView)
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
                                subscribeRoomsChange(stompClient,navView)
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

    fun sendViaWebSocket(stompClient: StompClient, roomModelRespond: RoomModelRespond){
        stompClient.send("/rooms/addRoom", Gson().toJson(roomModelRespond))
            .unsubscribeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},{t:Throwable->

            })
    }
    fun sendNewDeviceViaWebSocket(stompClient: StompClient, destionation:String ){
        stompClient.send(destionation)
            .unsubscribeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},{t:Throwable->

            })
    }

    fun subscribeRoomsChange(stompClient: StompClient, navView: NavigationView){
        stompClient.topic("/rooms/change")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
                val arrayType = object : TypeToken<List<Rooms>>() {}.type
                val received: List<Rooms> = Gson().fromJson(it.payload, arrayType)
                postRooms(received,navView)

                //  viewModel.receive(it.payload.toString())
                //Toast.makeText(context, it.payload.toString(), Toast.LENGTH_LONG).show()

            }, { t: Throwable? ->
                //  viewModel.receive(t.toString())
            })
        stompClient.topic("/device/newDevice")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
//                if(devicesList.contains(it.payload.toString())){
//
//                }else{
//                    devicesList.add(it.payload.toString())
//                    newDeviceAdapter.submitList(devicesList)
//                }
                deviceIp.postValue(it.payload.toString())

            }, { t: Throwable? ->

            })
    }



}