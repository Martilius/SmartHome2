package com.martilius.smarthome

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martilius.smarthome.models.Rooms
import com.martilius.smarthome.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(sharedPreferences: SharedPreferences, private val repository: Repository) : ViewModel() {

    val menuList = MutableLiveData<List<Rooms>>()
    val newTitle=MutableLiveData<String>()
    val roomAdded = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            val respond = repository.findRooms()
            if(!respond.isNullOrEmpty()){
                menuList.postValue(respond)
            }
        }
    }

    fun changeTitle(title:String){
        newTitle.value = title
    }

    fun AddRoom(roomName:String){
        viewModelScope.launch {
            val result = repository.addRoom(roomName)
            if (result.respond.equals("added")){
                roomAdded.postValue(roomName)
            }
        }

    }
}