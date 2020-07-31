package com.martilius.smarthome.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.repository.Repository
import kotlinx.coroutines.*
import javax.inject.Inject

class PawelsRoomViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferences: SharedPreferences,
    private val context: Context
) : ViewModel() {

    val configHL = MutableLiveData<MutableList<Configuration>>()
    val configLedRGB = MutableLiveData<List<Configuration>>()
    val configAlOnOff = MutableLiveData<List<Configuration>>()

    val configHLNull = MutableLiveData<Boolean>()
    val configLedRGBNull = MutableLiveData<Boolean>()
    val configAlOnOffNull = MutableLiveData<Boolean>()
val receivedMessage = MutableLiveData<String>()

//    init {
//        viewModelScope.launch {
//            val respond = repository.findConfigurationByRoom("pawla")
//            respond.forEach {
//                if(it.deviceType.equals("ledrgb")){
//                    respondLedRGB.add(it)
//                }else if(it.deviceType.equals("hl")){
//                    respondOnOff.add(it)
//                }else if(it.deviceType.equals("alonoff")){
//                    respondAlOnOff.add(it)
//                }
//            }
//            configLedRGB.postValue(respondLedRGB)
//            configHL.postValue(respondOnOff)
//            configAlOnOff.postValue(respondAlOnOff)
//        }
//
//
//    }

    fun receive(received:String){
        receivedMessage.postValue(received)
    }

    fun findDevices(roomName: String) {
        val respondHL: MutableList<Configuration> = arrayListOf()
        val respondLedRGB: MutableList<Configuration> = arrayListOf()
        val respondAlOnOff: MutableList<Configuration> = arrayListOf()


        viewModelScope.launch {
            val respond = repository.findConfigurationByRoom(roomName)

            respond.forEach {
                if (it.deviceType.equals("ledrgb")) {
                    respondLedRGB.add(it)
                } else if (it.deviceType.equals("hl")) {
                    respondHL.add(it)
                } else if (it.deviceType.equals("alonoff")) {
                    respondAlOnOff.add(it)
                }
            }

            if (respondLedRGB.isNullOrEmpty()) {
                configLedRGBNull.postValue(true)
            } else {
                configLedRGB.postValue(respondLedRGB)
            }
            if (respondAlOnOff.isNullOrEmpty()) {
                configAlOnOffNull.postValue(true)
            } else {
                configAlOnOff.postValue(respondAlOnOff)
            }
            if (respondHL.isNullOrEmpty()) {
                configHLNull.postValue(true)
            } else {
                configHL.postValue(respondHL)
            }


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