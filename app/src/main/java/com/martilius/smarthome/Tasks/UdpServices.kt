package com.martilius.smarthome.Tasks

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.viewModelScope
import com.martilius.smarthome.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetSocketAddress

class UdpServices() {

    fun sendWithoutRespond(message:String, context: Context){
        val ip = "192.168.2.173"
        val buffer = ByteArray(2048)
        val port = 8080
        var datagramSocket = DatagramSocket()
        GlobalScope.launch(Dispatchers.Default) {
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
//                val datagramPacketReceived = DatagramPacket(buffer,buffer.size)
//                datagramSocket.soTimeout = 2000
//                datagramSocket.receive(datagramPacketReceived)
//                val bla = String(buffer)


                datagramSocket.close()
            }catch (e: Exception){
                datagramSocket.close()
            }

        }
        //respond.postValue(String(buffer))
    }
}