package com.martilius.smarthome.ui.fragments

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.colorpicker.ColorPickerView
import com.google.android.material.slider.Slider
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.R
import com.martilius.smarthome.Tasks.ColorPickDialog
import com.martilius.smarthome.Tasks.UdpServices
import com.martilius.smarthome.models.Cupboard
import com.martilius.smarthome.ui.viewmodels.LoginViewModel
import com.martilius.smarthome.ui.viewmodels.PawelsRoomViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.pawels_room_fragment.*
import kotlinx.android.synthetic.main.pawels_room_fragment.view.*
import javax.inject.Inject
import kotlin.math.roundToInt

class PawelsRoomFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<PawelsRoomViewModel> { factory }


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

            additionalLightCustomButton.backgroundTintList =ColorStateList.valueOf(sharedPreferences?.getString(sendingAlIDN,Color.WHITE.toString()).toString().toInt())

            headerAdditionalLight.setOnClickListener{
                btAdditionalLightCardViewExpander.isChecked = btAdditionalLightCardViewExpander.isChecked != true
            }

            btPickCustomColorPawelsRoom.setOnClickListener {

                if (sharedPreferences != null) {
                    ColorPickDialog().showDialog(context,sharedPreferences,sendingAlIDN,additionalLightCustomButton, ivAdditionalLightCardView)
                }

            }

            srlPawelsRoom.setOnRefreshListener {
                viewModel.refresh()
            }
            with(viewModel){
                refresh.observe(viewLifecycleOwner, Observer {
                    val parts: List<String> = it.split(";")
                    btHeadLightSwitch.isChecked = parts[1].equals("on")
                    btAdditionalLightPawelsRoomSwitch.isChecked = parts[6].equals("on")
                    srlPawelsRoom.isRefreshing = false
                })
                initPawla.observe(viewLifecycleOwner, Observer {
                    val parts: List<String> = it.split(";")
                    btHeadLightSwitch.isChecked = parts[1].equals("on")
                    btAdditionalLightPawelsRoomSwitch.isChecked = parts[6].equals("on")
                    ivAdditionalLightCardView.setBackgroundColor(Color.rgb(parts[3].toInt(),parts[4].toInt(),parts[5].toInt()))
                })
            }

            btHeadLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked){
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2on)
                    if(btHeadLightSwitch.isPressed){
                        UdpServices().sendWithoutRespond("turn;${sendingHlID};on",context)
                    }
                }else if(!isChecked && btHeadLightSwitch.isPressed){
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2off)
                    if(btHeadLightSwitch.isPressed){
                        UdpServices().sendWithoutRespond("turn;${sendingHlID};off",context)
                    }
                }
            }

            btAdditionalLightPawelsRoomSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked && btAdditionalLightPawelsRoomSwitch.isPressed){
                        UdpServices().sendWithoutRespond("turn;${sendingAlIDN};on",context)
                }else if(!isChecked && btAdditionalLightPawelsRoomSwitch.isPressed){
                    UdpServices().sendWithoutRespond("turn;${sendingAlIDN};off",context)
                }
            }


            additionalLightWhiteButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.WHITE)
                UdpServices().sendWithoutRespond("set;${sendingAlIDN};255;255;255",context)
            }
            additionalLightRedButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.RED)
                UdpServices().sendWithoutRespond("set;${sendingAlIDN};255;0;0",context)}
            additionalLightGreenButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.GREEN)
                UdpServices().sendWithoutRespond("set;${sendingAlIDN};0;255;0",context)}
            additionalLightBlueButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.BLUE)
                UdpServices().sendWithoutRespond("set;${sendingAlIDN};0;0;255",context)}
            additionalLightCustomButton.setOnClickListener {
                val color = sharedPreferences?.getString(sendingAlIDN,Color.WHITE.toString()).toString().toInt()
                ivAdditionalLightCardView.setBackgroundColor(sharedPreferences?.getString(sendingAlIDN,Color.WHITE.toString()).toString().toInt())
                UdpServices().sendWithoutRespond("set;${sendingAlIDN};${color.red};${color.green};${color.blue}",context)}

            btAdditionalLightCardViewExpander.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked){
                    contentAdditionalLightCardView.visibility = View.VISIBLE
                    TransitionManager.beginDelayedTransition(cvAdditionalLight, AutoTransition())
                }else{
                    contentAdditionalLightCardView.visibility = View.GONE
                    TransitionManager.beginDelayedTransition(cvAdditionalLight, AutoTransition())
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(PawelsRoomViewModel::class.java)
        // TODO: Use the ViewModel
    }


}