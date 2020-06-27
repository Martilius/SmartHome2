package com.martilius.smarthome.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.R
import com.martilius.smarthome.ui.viewmodels.PawelsRoomViewModel
import kotlinx.android.synthetic.main.pawels_room_fragment.view.*

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

            btHeadLightCardViewExpander.setOnCheckedChangeListener { compoundButton, b ->
                if(b){
                    //TransitionManager.beginDelayedTransition(cvHeadLight, AutoTransition())
                }else{
                    //TransitionManager.beginDelayedTransition(cvHeadLight, AutoTransition())
                }
            }

            btAdditionalLightCardViewExpander.setOnCheckedChangeListener { compoundButton, b ->
                if(b){
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