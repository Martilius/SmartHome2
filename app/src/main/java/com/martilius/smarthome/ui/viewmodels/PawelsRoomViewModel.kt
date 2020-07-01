package com.martilius.smarthome.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martilius.smarthome.R
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetSocketAddress
import java.nio.Buffer
import javax.inject.Inject

class PawelsRoomViewModel @Inject constructor(private val sharedPreferences: SharedPreferences, private val context:Context):ViewModel() {

    val respond = MutableLiveData<String>()

    init {

    }
    fun Send(message:String){
        val ip = "192.168.2.173"
        val buffer = ByteArray(2048)
        val port = 8080
        var datagramSocket = DatagramSocket()
        viewModelScope.launch(Dispatchers.Default) {
            try{
                val local = Inet4Address.getByName(context.getString(R.string.ip_address))
                if(datagramSocket.isBound){
                    datagramSocket = DatagramSocket(null)
                    datagramSocket.reuseAddress = true
                    datagramSocket.bind(InetSocketAddress(port))
                }else{
                    datagramSocket.bind(InetSocketAddress(port))
                }

                val datagramPacketSend = DatagramPacket(message.toByteArray(),message.length,local,port)
                datagramSocket.send(datagramPacketSend)
                val datagramPacketReceived = DatagramPacket(buffer,buffer.size)
                datagramSocket.soTimeout = 2000
                datagramSocket.receive(datagramPacketReceived)
                val bla = String(buffer)
                withContext(Dispatchers.Main){
                    respond.postValue(bla)
                }

                datagramSocket.close()
            }catch (e: Exception){
                datagramSocket.close()
                withContext(Dispatchers.Main){
                    respond.postValue(e.toString())
                }




            }

        }
        //respond.postValue(String(buffer))
    }

}