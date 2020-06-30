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
import androidx.core.graphics.ColorUtils
import com.github.dhaval2404.colorpicker.ColorPickerView
import com.google.android.material.slider.Slider
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.R
import com.martilius.smarthome.Tasks.ColorPickDialog
import com.martilius.smarthome.ui.viewmodels.PawelsRoomViewModel
import kotlinx.android.synthetic.main.pawels_room_fragment.*
import kotlinx.android.synthetic.main.pawels_room_fragment.view.*
import kotlin.math.roundToInt

class PawelsRoomFragment : Fragment() {

    companion object {
        fun newInstance() = PawelsRoomFragment()
    }

    private lateinit var viewModel: PawelsRoomViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pawels_room_fragment, container, false).apply {
            enterTransition = MaterialFadeThrough().setDuration(500L)
            exitTransition = MaterialFadeThrough().setDuration(500L)
            val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
            btHeadLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked){
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2on)
                    //TransitionManager.beginDelayedTransition(cvHeadLight, AutoTransition())
                }else{
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2off)
                    //TransitionManager.beginDelayedTransition(cvHeadLight, AutoTransition())
                }
            }
            additionalLightCustomButton.backgroundTintList =ColorStateList.valueOf(sharedPreferences?.getString("PawelsRoomCustomColor",Color.WHITE.toString()).toString().toInt())

            headerAdditionalLight.setOnClickListener{
                btAdditionalLightCardViewExpander.isChecked = btAdditionalLightCardViewExpander.isChecked != true
            }

            btPickCustomColorPawelsRoom.setOnClickListener {

                if (sharedPreferences != null) {
                    ColorPickDialog().showDialog(context,sharedPreferences,"PawelsRoomCustomColor",additionalLightCustomButton)
                }

            }

            srlPawelsRoom.setOnRefreshListener {
                srlPawelsRoom.isRefreshing = false
            }


            additionalLightWhiteButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.WHITE)
            sharedPreferences?.edit()?.putString("PawelsRoomCustomColor",Color.RED.toString())?.apply()}
            additionalLightRedButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.RED) }
            additionalLightGreenButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.GREEN) }
            additionalLightBlueButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.BLUE) }
            additionalLightCustomButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(sharedPreferences?.getString("PawelsRoomCustomColor",Color.WHITE.toString()).toString().toInt()) }

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
        viewModel = ViewModelProviders.of(this).get(PawelsRoomViewModel::class.java)
        // TODO: Use the ViewModel
    }


}