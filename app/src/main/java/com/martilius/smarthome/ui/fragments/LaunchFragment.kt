package com.martilius.smarthome.ui.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.martilius.smarthome.MainActivity
import com.martilius.smarthome.MainViewModel
import com.martilius.smarthome.R
import com.martilius.smarthome.ui.viewmodels.LaunchViewModel

class LaunchFragment : Fragment() {

    companion object {
        fun newInstance() = LaunchFragment()
    }

    private lateinit var viewModel: LaunchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.launch_fragment, container, false).apply {
            val sharedPreferences = activity?.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
            val mainViewModel =
                activity?.let { ViewModelProvider(it).get(MainViewModel::class.java) }!!
            // GlobalScope.launch {
            with(mainViewModel) {
                initDone.observe(viewLifecycleOwner, Observer {


                        if(sharedPreferences?.getBoolean("logged",false)!! && MainActivity.Companion.internetConnection){
                            mainViewModel.init()
                            findNavController().navigate(R.id.action_launchFragment_to_nav_pawels_room)
                        }else{
                            findNavController().navigate(R.id.action_launchFragment_to_nav_login)
                        }
//
                })
            }

            //delay(1500)
            //}

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LaunchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}