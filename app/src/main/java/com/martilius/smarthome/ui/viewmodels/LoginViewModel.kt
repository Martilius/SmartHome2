package com.martilius.smarthome.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martilius.smarthome.MainActivity
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.models.Respond
import com.martilius.smarthome.repository.Repository
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import ua.naiksoftware.stomp.provider.OkHttpConnectionProvider
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: Repository,
    sharedPreferences: SharedPreferences,
    context: Context
) : ViewModel() {

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

    fun login(login: String, password: String, context: Context) {
        if (MainActivity.internetConnection) {
            viewModelScope.launch {
                val respond = repository.loginRequest(login, password)
                loginRespond.postValue(respond)
                println("respond: $respond")
            }
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

    }

    fun register(login: String, password: String, context: Context) {
        if (MainActivity.internetConnection) {
            viewModelScope.launch {
                val respond = repository.registerRequest(login, password)
                registerRespond.postValue(respond)
            }
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }
}