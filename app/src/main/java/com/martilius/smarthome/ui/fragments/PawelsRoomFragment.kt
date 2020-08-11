package com.martilius.smarthome.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.MaterialFadeThrough
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.martilius.smarthome.MainViewModel
import com.martilius.smarthome.R
import com.martilius.smarthome.adapters.HeadLightAdapter
import com.martilius.smarthome.adapters.LedAdapter
import com.martilius.smarthome.adapters.OnOffAdapter
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.ui.viewmodels.PawelsRoomViewModel
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_headlight.view.*
import kotlinx.android.synthetic.main.pawels_room_fragment.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import javax.inject.Inject


class PawelsRoomFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<PawelsRoomViewModel> { factory }
    //private val mainViewModel by viewModels<MainViewModel> { factory }


    private val ledAdapter by lazy {
        LedAdapter {
        }
    }

    private val headLightAdapter by lazy {
        HeadLightAdapter {

        }
    }

    private val onOffAdapter by lazy {
        OnOffAdapter {

        }
    }
    lateinit var webSocketClient: WebSocketClient

    val stompClient: StompClient = Stomp.over(
        Stomp.ConnectionProvider.OKHTTP,
        "ws://192.168.2.174:9999/mywebsocket/websocket"
    )

    lateinit var disposable: Disposable
    private var compositeDisposable: CompositeDisposable? =null

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pawels_room_fragment, container, false).apply {
            enterTransition = MaterialFadeThrough().setDuration(500L)
            exitTransition = MaterialFadeThrough().setDuration(500L)
            val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

            rvLedLight.adapter = ledAdapter
            rvHeadLight.adapter = headLightAdapter
            rvOnOffLight.adapter = onOffAdapter
            with(viewModel) {
                configLedRGB.observe(viewLifecycleOwner, Observer {
                    ledAdapter.submitList(it)
                })
                configHL.observe(viewLifecycleOwner, Observer {
                    headLightAdapter.submitList(it)
                })
                configAlOnOff.observe(viewLifecycleOwner, Observer {
                    onOffAdapter.submitList(it)
                })
                configLedRGBNull.observe(viewLifecycleOwner, Observer {
                    if (it) {
                        ledAdapter.submitList(null)
                    }
                })
                configHLNull.observe(viewLifecycleOwner, Observer {
                    if (it) {
                        headLightAdapter.submitList(null)
                    }
                })
                configAlOnOffNull.observe(viewLifecycleOwner, Observer {
                    if (it) {
                        onOffAdapter.submitList(null)
                    }
                })
                receivedMessage.observe(viewLifecycleOwner, Observer {
                    println(it.toString())
                })
            }
            val mainViewModel =
                activity?.let { ViewModelProvider(it).get(MainViewModel::class.java) }!!

            with(mainViewModel) {
                newTitle.observe(viewLifecycleOwner, Observer {
                    viewModel.findDevices(it)
                    resetSubscription()
                    subscription(it)
                    connection(it)
                    //Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                })
            }
        }
    }

    private fun subscription(roomName: String){
        disposable = stompClient.topic("/room/${roomName}")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
                val arrayType = object : TypeToken<List<Configuration>>() {}.type
                val received: List<Configuration> = Gson().fromJson(it.payload, arrayType)
                viewModel.devicesActualization(received)
            }, { t: Throwable? ->

            })
        compositeDisposable?.add(disposable)
    }

    private fun resetSubscription(){
        compositeDisposable?.dispose()
        compositeDisposable = CompositeDisposable()
    }

    @SuppressLint("CheckResult")
    @InternalCoroutinesApi
    private fun connection(roomName: String) {

            stompClient.withServerHeartbeat(10000)

        stompClient.connect()
        compositeDisposable?.add(stompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe({
                    when (it.type) {
                        LifecycleEvent.Type.OPENED -> {
                            viewModel.receive(it.type.name)
                        }
                        LifecycleEvent.Type.ERROR -> {
                            viewModel.receive(it.type.name)
                        }
                        LifecycleEvent.Type.CLOSED -> {
                            val connectivityManager =
                                context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                            val networkRequest = NetworkRequest.Builder()
                                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                                .build()
                            var networkCallback = object : ConnectivityManager.NetworkCallback() {
                                override fun onAvailable(network: Network) {
                                    subscription(roomName)
                                    stompClient.connect()
                                    viewModel.findDevices(roomName)
                                    connectivityManager.unregisterNetworkCallback(this)
                                }

                            }


                            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
                        }
                    }
                }, { t: Throwable ->
                    viewModel.receive(t.toString())
                }))

    }

}