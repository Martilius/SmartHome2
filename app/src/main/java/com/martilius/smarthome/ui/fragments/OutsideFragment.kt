package com.martilius.smarthome.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.R
import com.martilius.smarthome.ui.viewmodels.OutsideViewModel

class OutsideFragment : Fragment() {

    private lateinit var outsideViewModel: OutsideViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        outsideViewModel = ViewModelProvider(this).get(OutsideViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_outside, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        enterTransition = MaterialFadeThrough().setDuration(500L)
        exitTransition = MaterialFadeThrough().setDuration(500L)
        outsideViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}