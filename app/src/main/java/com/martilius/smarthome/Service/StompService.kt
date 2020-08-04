package com.martilius.smarthome.Service

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient

class StompService {

    lateinit var stompClient:StompClient

    fun initial(){
        stompClient = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            "ws://192.168.2.174:9999/mywebsocket/websocket"
        )
        stompClient.connect()
    }

    fun sendMessage(destination : String){
        stompClient.send(destination)
            .unsubscribeOn(Schedulers.newThread())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}