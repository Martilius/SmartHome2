package com.martilius.smarthome.Tasks

import android.content.Context
import android.widget.Toast
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class client(serverUri: URI?, val context: Context) : WebSocketClient(serverUri) {
    override fun onOpen(handshakedata: ServerHandshake?) {
        Toast.makeText(context, "opened", Toast.LENGTH_LONG).show()
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Toast.makeText(context, "closed", Toast.LENGTH_LONG).show()
    }

    override fun onMessage(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onError(ex: Exception?) {
    }
}