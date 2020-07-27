package com.martilius.smarthome.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.models.Respond
import com.martilius.smarthome.repository.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel   @Inject constructor(private val repository: Repository,sharedPreferences: SharedPreferences, context: Context):ViewModel() {

    val testpost = MutableLiveData<List<Configuration>>()
    val loginRespond = MutableLiveData<Respond>()
    val registerRespond = MutableLiveData<Respond>()

//init {
//        viewModelScope.launch {
//            val test = repository.findConfigurationByRoom("pawla")
//            if(test!=null){
//            testpost.postValue(test)
//            }
//        }
//
//    }

    fun login(login:String, password:String){
        viewModelScope.launch {
            val respond = repository.loginRequest(login,password)
            if(respond!=null){
                loginRespond.postValue(respond)
            }
        }
    }

    fun register(login:String, password:String){
        viewModelScope.launch {
            val respond = repository.registerRequest(login,password)
            if(respond!=null){
                registerRespond.postValue(respond)
            }
        }
    }
}