package com.martilius.smarthome.ui.fragments

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.martilius.smarthome.MainViewModel
import com.martilius.smarthome.R
import com.martilius.smarthome.adapters.RoomSettingsAdapter
import com.martilius.smarthome.ui.viewmodels.SettingsViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.settings_fragment.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import javax.inject.Inject

class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val viewModel by viewModels<SettingsViewModel> { factory }

    val roomSettingsAdapter by lazy {
        RoomSettingsAdapter{

        }
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false).apply {
            rvRoomSettings.adapter = roomSettingsAdapter
            //viewModel.connection(stompClient,roomSettingsAdapter.currentList,context)
            val thisView = this
            roomSettingsHeader.setOnClickListener {
                roomSettingsExpander.isChecked = !roomSettingsExpander.isChecked
            }
            roomSettingsExpander.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked){
                    TransitionManager.beginDelayedTransition(this as ViewGroup?, AutoTransition())
                    roomSettingsExpandedLayout.visibility = View.VISIBLE
                }else{
                    TransitionManager.beginDelayedTransition(this as ViewGroup?, AutoTransition())
                    roomSettingsExpandedLayout.visibility = View.GONE
                }
            }

            adminSettingsHeader.setOnClickListener {
                adminSettingsExpander.isChecked = !adminSettingsExpander.isChecked
            }
            adminSettingsExpander.setOnCheckedChangeListener { compoundButton, isChecked ->
                if(isChecked){
                    TransitionManager.beginDelayedTransition(this as ViewGroup?, AutoTransition())
                    adminSettingsExpandedLayout.visibility = View.VISIBLE
                }else{
                    TransitionManager.beginDelayedTransition(this as ViewGroup?, AutoTransition())
                    adminSettingsExpandedLayout.visibility = View.GONE
                }
            }



            with(viewModel){
                roomsRespond.observe(viewLifecycleOwner, Observer {
                    roomSettingsAdapter.submitList(it)
                })
            }
            val mainViewModel =
                activity?.let { ViewModelProvider(it).get(MainViewModel::class.java) }!!
            with(mainViewModel){
                roomCountChanged.observe(viewLifecycleOwner, Observer {
                    TransitionManager.beginDelayedTransition(thisView as ViewGroup?, AutoTransition())
                    roomSettingsAdapter.submitList(it)
                })
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}