package com.martilius.smarthome.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martilius.smarthome.R
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.repository.Repository
import kotlinx.coroutines.*
import java.net.*
import javax.inject.Inject
import kotlin.Exception

class PawelsRoomViewModel @Inject constructor(private val repository: Repository, private val sharedPreferences: SharedPreferences, private val context:Context):ViewModel() {

    val respond = MutableLiveData<String>()
    val hlRespond = MutableLiveData<String>()
    val alRespond = MutableLiveData<String>()
    val initPawla = MutableLiveData<String>()
    val refresh = MutableLiveData<String>()
    val respondOnOff: MutableList<Configuration> = arrayListOf()
    val respondLedRGB : MutableList<Configuration> = arrayListOf()
    val configOnOff = MutableLiveData<MutableList<Configuration>>()
    val configLedRGB = MutableLiveData<List<Configuration>>()
    init {
        viewModelScope.launch {
            val respond = repository.findConfigurationByRoom("pawla")
            respond.forEach {
                if(it.deviceType.equals("ledrgb")){
                    respondLedRGB.add(it)
                }else if(it.deviceType.equals("hlonoff")){
                    respondOnOff.add(it)
                }
            }
            configLedRGB.postValue(respondLedRGB)
            configOnOff.postValue(respondOnOff)
        }

    }


//    init {
//        SendWithResult(context.getString(R.string.pawla_init), initPawla)
//    }
//
//    fun refresh(){
//        SendWithResult(context.getString(R.string.pawla_init), refresh)
//    }
//
//    fun SendWithResult(message: String, mutableLiveData: MutableLiveData<String>) {
//        val ip = "192.168.2.173"
//        val buffer = ByteArray(2048)
//        val port = 8080
//        var datagramSocket = DatagramSocket(null)
//        viewModelScope.launch(Dispatchers.Default) {
//            try {
//                val local = Inet4Address.getByName(context.getString(R.string.ip_address))
//                if (!datagramSocket.isBound) {
//                    datagramSocket = DatagramSocket(null)
//                    datagramSocket.reuseAddress = true
//                    datagramSocket.bind(InetSocketAddress(port))
//                } else {
//                    datagramSocket.bind(InetSocketAddress(port))
//                }
//
//                val datagramPacketSend =
//                    DatagramPacket(message.toByteArray(), message.length, local, port)
//                datagramSocket.send(datagramPacketSend)
//                val datagramPacketReceived = DatagramPacket(buffer, buffer.size)
//                datagramSocket.soTimeout = 100
//                datagramSocket.receive(datagramPacketReceived)
//                val received = String(buffer,0,datagramPacketReceived.length)
//                withContext(Dispatchers.Main) {
//                    mutableLiveData.postValue(received)
//                }
//                datagramSocket.close()
//            } catch (e: Exception) {
//                datagramSocket.close()
//                withContext(Dispatchers.Main) {
//                    //mutableLiveData.postValue(e.toString())
//                }
//                SendWithResult(message, mutableLiveData)
//            }
//        }
//
//    }
}