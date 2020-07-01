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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.colorpicker.ColorPickerView
import com.google.android.material.slider.Slider
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.R
import com.martilius.smarthome.Tasks.ColorPickDialog
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
            btHeadLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked){
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2on)
                    //TransitionManager.beginDelayedTransition(cvHeadLight, AutoTransition())
                }else{
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2off)
                    //TransitionManager.beginDelayedTransition(cvHeadLight, AutoTransition())
                }
            }
            additionalLightCustomButton.backgroundTintList =ColorStateList.valueOf(sharedPreferences?.getString("alpawla",Color.WHITE.toString()).toString().toInt())

            headerAdditionalLight.setOnClickListener{
                btAdditionalLightCardViewExpander.isChecked = btAdditionalLightCardViewExpander.isChecked != true
            }
            val colorpickdial = ColorPickDialog()

            btPickCustomColorPawelsRoom.setOnClickListener {

                if (sharedPreferences != null) {
                    ColorPickDialog().showDialog(context,sharedPreferences,"alpawla",additionalLightCustomButton)
                }

            }

            srlPawelsRoom.setOnRefreshListener {
                viewModel.Send("startup;alpawla")
                srlPawelsRoom.isRefreshing = false
            }
            with(viewModel){
                respond.observe(viewLifecycleOwner, Observer {
                    Toast.makeText(context,it,Toast.LENGTH_LONG).show()
                })
            }


            additionalLightWhiteButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.WHITE) }
            additionalLightRedButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.RED) }
            additionalLightGreenButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.GREEN) }
            additionalLightBlueButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.BLUE) }
            additionalLightCustomButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(sharedPreferences?.getString("alpawla",Color.WHITE.toString()).toString().toInt()) }

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