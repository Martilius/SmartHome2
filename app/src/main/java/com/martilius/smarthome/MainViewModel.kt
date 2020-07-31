package com.martilius.smarthome

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
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


    init {
        viewModelScope.launch {
            val findRoomsRespond = repository.findRooms()
            if(!findRoomsRespond.isNullOrEmpty()){
                menuList.postValue(findRoomsRespond)
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


//    fun RoomCountChanged(roomName:String){
//        viewModelScope.launch {
//            val result = repository.addRoom(roomName)
//            if (result.respond.equals("added")){
//                roomAdded.postValue(roomName)
//            }
//        }
//
//    }

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
    fun connection(stompClient: StompClient, navView: NavigationView) {
//            val stompClient: StompClient = Stomp.over(
//                Stomp.ConnectionProvider.OKHTTP,
//                "ws://192.168.2.174:9999/mywebsocket/websocket"
//            )

        stompClient.withServerHeartbeat(10000)
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
                        //   viewModel.receive(it.type.name)
                    }
                }
            }, { t: Throwable ->
                // viewModel.receive(t.toString())
            })


    }

    fun sendViaWebSocket(stompClient: StompClient, editText: EditText){
        stompClient.send("/rooms/add",editText.text.toString())
            .unsubscribeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun addDeviceDialog(deviceTypes: List<DeviceType>, navView: NavigationView, context: Context, actionBar: ActionBar, newDeviceAdapter: NewDeviceAdapter ){
        var roomsList = mutableListOf<String>()
        var deviceTypeList = mutableListOf<String>()
        deviceTypes.forEach {
            deviceTypeList.add(it.deviceType.toString())
        }
        navView.menu.forEach {
            roomsList.add(it.title.toString())
        }
        roomsList.remove(roomsList.last())
        val roomsAdapter = ArrayAdapter<String>(context, R.layout.dropdown_menu_item,roomsList)
        val deviceTypeAdapter = ArrayAdapter<String>(context, R.layout.dropdown_menu_item,deviceTypeList)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.adding_devices_dialog, null)

        dialogView.roomAutoCompleteTextView.setAdapter(roomsAdapter)
        dialogView.deviceTypeAutoCompleteTextView.setAdapter(deviceTypeAdapter)
        dialogView.roomAutoCompleteTextView.setText(actionBar.title,false)
        dialogView.rvNewDevice.adapter = newDeviceAdapter
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogView)
        dialog.show()
        dialogView.btCancelAddDevice.setOnClickListener { dialog.dismiss() }
        dialogView.btAddDevice.setOnClickListener { dialog.dismiss()
            if(newDeviceAdapter.getSelected()!=-1){
                val selected =  newDeviceAdapter.currentList.get(newDeviceAdapter.getSelected())
                Toast.makeText(context,selected.ip, Toast.LENGTH_LONG).show()
            }
        }
        newDeviceAdapter.submitList(listOf(
            NewDevice(id = 1,ip = "192.168.1.1"),
            NewDevice(id = 2,ip = "192.168.2.2")
        ))
    }

}