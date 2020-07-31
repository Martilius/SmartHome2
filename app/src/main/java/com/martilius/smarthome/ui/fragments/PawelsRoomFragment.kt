package com.martilius.smarthome.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
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

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pawels_room_fragment, container, false).apply {
            enterTransition = MaterialFadeThrough().setDuration(500L)
            exitTransition = MaterialFadeThrough().setDuration(500L)
            val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
            lateinit var roomTitle:String
            //val request = Request.Builder().url("ws://localhost:8080/myWebServer").build()
            //val listener = EchoServer();
            //val ws = OkHttpClient().newWebSocket(request,listener)
            //createWebSocketClient()
            //val stomp = StompClient()


            testButton.setOnClickListener {
                stompClient.send("/app/type","bbbb")
                    .unsubscribeOn(Schedulers.newThread())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }

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
                    //if(it.equals("closed!")||it.equals("error!")){
                    test.text = it.toString()
                    //}else{
                    //Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    //}
                })
            }
            val mainViewModel =
                activity?.let { ViewModelProvider(it).get(MainViewModel::class.java) }!!

            with(mainViewModel) {
                newTitle.observe(viewLifecycleOwner, Observer {
                    viewModel.findDevices(it)
                    roomTitle = it
                    connection(roomTitle)
                    //Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                })
            }

            //TransitionManager.beginDelayedTransition(rvPawla, AutoTransition())
//            val udpServices = UdpServices()
//            additionalLightCustomButton.backgroundTintList =ColorStateList.valueOf(sharedPreferences?.getString(sendingAlIDN,Color.WHITE.toString()).toString().toInt())
//
//            headerAdditionalLight.setOnClickListener{
//                btAdditionalLightCardViewExpander.isChecked = btAdditionalLightCardViewExpander.isChecked != true
//            }
//
//            btPickCustomColorPawelsRoom.setOnClickListener {
//
//                if (sharedPreferences != null) {
//                    ColorPickDialog().showDialog(context,sharedPreferences,sendingAlIDN,additionalLightCustomButton, ivAdditionalLightCardView)
//                }
//
//            }
//
//            srlPawelsRoom.setOnRefreshListener {
//                //viewModel.refresh()
//            }
//            with(viewModel){
//                refresh.observe(viewLifecycleOwner, Observer {
//                    val parts: List<String> = it.split(";")
//                    btHeadLightSwitch.isChecked = parts[1].equals("on")
//                    btAdditionalLightPawelsRoomSwitch.isChecked = parts[6].equals("on")
//                    srlPawelsRoom.isRefreshing = false
//                })
//                initPawla.observe(viewLifecycleOwner, Observer {
//                    val parts: List<String> = it.split(";")
//                    btHeadLightSwitch.isChecked = parts[1].equals("on")
//                    btAdditionalLightPawelsRoomSwitch.isChecked = parts[6].equals("on")
//                    ivAdditionalLightCardView.setBackgroundColor(Color.rgb(parts[3].toInt(),parts[4].toInt(),parts[5].toInt()))
//                })
//                configLedRGB.observe(viewLifecycleOwner, Observer {
//
//                })
//                configOnOff.observe(viewLifecycleOwner, Observer {
//
//                })
//            }
//
//            btHeadLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
//                if(isChecked){
//                    ivHeadLightCardView.setImageResource(R.drawable.lampv2on)
//                    if(btHeadLightSwitch.isPressed){
//                        udpServices.sendWithResult("turn;${sendingHlID};on",context)
//                        //UdpServices().sendWithoutRespond("turn;${sendingHlID};on",context)
//                    }
//                }else if(!isChecked && btHeadLightSwitch.isPressed){
//                    ivHeadLightCardView.setImageResource(R.drawable.lampv2off)
//                    if(btHeadLightSwitch.isPressed){
//                        UdpServices().sendWithoutRespond("turn;${sendingHlID};off",context)
//                    }
//                }
//            }
//
//            udpServices.result.observe(viewLifecycleOwner, Observer { Toast.makeText(context,it.toString(),Toast.LENGTH_LONG).show() })
//
//            btAdditionalLightPawelsRoomSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
//                if(isChecked && btAdditionalLightPawelsRoomSwitch.isPressed){
//                        UdpServices().sendWithoutRespond("turn;${sendingAlIDN};on",context)
//                }else if(!isChecked && btAdditionalLightPawelsRoomSwitch.isPressed){
//                    UdpServices().sendWithoutRespond("turn;${sendingAlIDN};off",context)
//                }
//            }
//
//
//            additionalLightWhiteButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.WHITE)
//                UdpServices().sendWithoutRespond("set;${sendingAlIDN};255;255;255",context)
//            }
//            additionalLightRedButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.RED)
//                UdpServices().sendWithoutRespond("set;${sendingAlIDN};255;0;0",context)}
//            additionalLightGreenButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.GREEN)
//                UdpServices().sendWithoutRespond("set;${sendingAlIDN};0;255;0",context)}
//            additionalLightBlueButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.BLUE)
//                UdpServices().sendWithoutRespond("set;${sendingAlIDN};0;0;255",context)}
//            additionalLightCustomButton.setOnClickListener {
//                val color = sharedPreferences?.getString(sendingAlIDN,Color.WHITE.toString()).toString().toInt()
//                ivAdditionalLightCardView.setBackgroundColor(sharedPreferences?.getString(sendingAlIDN,Color.WHITE.toString()).toString().toInt())
//                UdpServices().sendWithoutRespond("set;${sendingAlIDN};${color.red};${color.green};${color.blue}",context)}
//
//            btAdditionalLightCardViewExpander.setOnCheckedChangeListener { compoundButton, isChecked ->
//                if(isChecked){
//                    contentAdditionalLightCardView.visibility = View.VISIBLE
//                    TransitionManager.beginDelayedTransition(cvAdditionalLight, AutoTransition())
//                }else{
//                    contentAdditionalLightCardView.visibility = View.GONE
//                    TransitionManager.beginDelayedTransition(cvAdditionalLight, AutoTransition())
//                }
//            }
        }
    }

    @SuppressLint("CheckResult")
    @InternalCoroutinesApi
    private fun connection(roomName:String) {
//            val stompClient: StompClient = Stomp.over(
//                Stomp.ConnectionProvider.OKHTTP,
//                "ws://192.168.2.174:9999/mywebsocket/websocket"
//            )

            stompClient.withServerHeartbeat(10000)
            stompClient.topic("/topic/test")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe({
                    viewModel.receive(it.payload.toString())
                    //Toast.makeText(context, it.payload.toString(), Toast.LENGTH_LONG).show()

                }, { t: Throwable? ->
                    viewModel.receive(t.toString())
                })

        stompClient.topic("/room/${roomName}")
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe({
                val arrayType = object : TypeToken<List<Configuration>>() {}.type
                val received: List<Configuration> = Gson().fromJson(it.payload, arrayType)
                viewModel.devicesActualization(received)
            }, { t: Throwable? ->

            })

        stompClient.connect()
        stompClient.lifecycle()
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
                            viewModel.receive(it.type.name)
                        }
                    }
                }, { t: Throwable ->
                    viewModel.receive(t.toString())
                })
            stompClient.send("/app/type","bbbb")
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()


    }

}