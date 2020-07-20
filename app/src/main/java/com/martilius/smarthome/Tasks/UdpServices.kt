package com.martilius.smarthome.Tasks

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.martilius.smarthome.R
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.net.InetSocketAddress


class UdpServices {
    val result = MutableLiveData<String>()
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

    fun sendWithResult(message: String, context: Context) {
        val ip = "192.168.2.173"
        val buffer = ByteArray(2048)
        val port = 8080
        var datagramSocket = DatagramSocket(null)
        GlobalScope.launch() {
            try {
                val local = Inet4Address.getByName(context.getString(R.string.ip_address))
                if (!datagramSocket.isBound) {
                    datagramSocket = DatagramSocket(null)
                    datagramSocket.reuseAddress = true
                    datagramSocket.broadcast = true
                    datagramSocket.bind(InetSocketAddress(port))
                } else {
                    datagramSocket.bind(InetSocketAddress(port))
                }

                val datagramPacketSend =
                    DatagramPacket(message.toByteArray(), message.length, local, port)
                datagramSocket.send(datagramPacketSend)
                val datagramPacketReceived = DatagramPacket(buffer, buffer.size)
                datagramSocket.soTimeout = 200
                datagramSocket.receive(datagramPacketReceived)
                val received = String(buffer,0,datagramPacketReceived.length)
                withContext(Dispatchers.Main) {

                        result.postValue(received)

                }
                //datagramSocket.close()
            } catch (e: Exception) {
                if(e.message.equals("Poll timed out"))
                datagramSocket.close()
//                withContext(Dispatchers.Main) {
//                   // result.postValue(e.toString())
//                }
                sendWithResult(message, context)
            }
            finally {
                if (datagramSocket!= null){
                    datagramSocket.close()
                }
            }
        }

    }
}