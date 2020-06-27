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
import com.google.android.material.transition.MaterialSharedAxis
import com.martilius.smarthome.R
import com.martilius.smarthome.ui.viewmodels.SaloonViewModel

class SaloonFragment : Fragment() {

    private lateinit var saloonViewModel: SaloonViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        saloonViewModel = ViewModelProvider(this).get(SaloonViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_saloon, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        enterTransition = MaterialFadeThrough().setDuration(500L)
        exitTransition = MaterialFadeThrough().setDuration(500L)
        saloonViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}