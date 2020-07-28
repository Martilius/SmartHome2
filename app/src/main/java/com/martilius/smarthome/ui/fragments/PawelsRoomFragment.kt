package com.martilius.smarthome.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.MainActivity
import com.martilius.smarthome.MainViewModel
import com.martilius.smarthome.R
import com.martilius.smarthome.adapters.LedAdapter
import com.martilius.smarthome.repository.Repository
import com.martilius.smarthome.repository.remote.ConfigurationService
import com.martilius.smarthome.repository.remote.UserService
import com.martilius.smarthome.ui.viewmodels.PawelsRoomViewModel
import dagger.Provides
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.item_led_rgb.*
import kotlinx.android.synthetic.main.item_led_rgb.view.*
import kotlinx.android.synthetic.main.pawels_room_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pawels_room_fragment, container, false).apply {
            enterTransition = MaterialFadeThrough().setDuration(500L)
            exitTransition = MaterialFadeThrough().setDuration(500L)
            val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
            val sendingHlID = "hlpawla"
            val sendingAlIDN = "alpawla"
            rvPawla.adapter = ledAdapter
            with(viewModel) {
                configLedRGB.observe(viewLifecycleOwner, Observer {
                    ledAdapter.submitList(it)
                })
            }
             val mainViewModel = activity?.let { ViewModelProvider(it).get(MainViewModel::class.java) }!!

            with(mainViewModel) {
                newTitle.observe(viewLifecycleOwner, Observer {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(PawelsRoomViewModel::class.java)
        // TODO: Use the ViewModel
    }


}