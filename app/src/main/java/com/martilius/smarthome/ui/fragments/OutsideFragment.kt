package com.martilius.smarthome.ui.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.R
import com.martilius.smarthome.Tasks.ColorPickDialog
import com.martilius.smarthome.ui.viewmodels.LoginViewModel
import com.martilius.smarthome.ui.viewmodels.OutsideViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.outside_fragment.view.*
import kotlinx.android.synthetic.main.pawels_room_fragment.view.*
import javax.inject.Inject

class OutsideFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<OutsideViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.outside_fragment, container, false).apply {
            enterTransition = MaterialFadeThrough().setDuration(500L)
            exitTransition = MaterialFadeThrough().setDuration(500L)
            val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
            additionalLightCustomButtonAltana.backgroundTintList =
                ColorStateList.valueOf(sharedPreferences?.getString("alaltana",Color.WHITE.toString()).toString().toInt())
            headerAdditionalLightAltana.setOnClickListener{
                btAdditionalLightCardViewExpanderAltana.isChecked = btAdditionalLightCardViewExpanderAltana.isChecked != true
            }

            btPickCustomColorPawelsRoomAltana.setOnClickListener {

                if (sharedPreferences != null) {
                    ColorPickDialog().showDialog(context,sharedPreferences,"alaltana",additionalLightCustomButtonAltana)
                }

            }

            srlOutside.setOnRefreshListener {
                srlPawelsRoom.isRefreshing = false
            }

            btAdditionalLightCardViewExpanderAltana.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked){
                    contentAdditionalLightCardViewAltana.visibility = View.VISIBLE
                    TransitionManager.beginDelayedTransition(cvAltana, AutoTransition())
                }else{
                    contentAdditionalLightCardViewAltana.visibility = View.GONE
                    TransitionManager.beginDelayedTransition(cvAltana, AutoTransition())
                }
            }

            additionalLightWhiteButtonAltana.setOnClickListener { ivAdditionalLightCardViewAltana.setBackgroundColor(
                Color.WHITE) }
            additionalLightRedButtonAltana.setOnClickListener { ivAdditionalLightCardViewAltana.setBackgroundColor(
                Color.RED) }
            additionalLightGreenButtonAltana.setOnClickListener { ivAdditionalLightCardViewAltana.setBackgroundColor(
                Color.GREEN) }
            additionalLightBlueButtonAltana.setOnClickListener { ivAdditionalLightCardViewAltana.setBackgroundColor(
                Color.BLUE) }
            additionalLightCustomButtonAltana.setOnClickListener { ivAdditionalLightCardViewAltana.setBackgroundColor(sharedPreferences?.getString("alaltana",Color.WHITE.toString()).toString().toInt()) }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(OutsideViewModel::class.java)
        // TODO: Use the ViewModel
    }

}