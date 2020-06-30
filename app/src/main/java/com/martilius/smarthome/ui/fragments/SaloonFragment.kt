package com.martilius.smarthome.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.R
import com.martilius.smarthome.ui.viewmodels.SaloonViewModel
import kotlinx.android.synthetic.main.saloon_fragment.view.*

class SaloonFragment : Fragment() {

    companion object {
        fun newInstance() = SaloonFragment()
    }

    private lateinit var viewModel: SaloonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saloon_fragment, container, false).apply {
            enterTransition = MaterialFadeThrough().setDuration(500L)
            exitTransition = MaterialFadeThrough().setDuration(500L)
            button.setOnClickListener {
                Toast.makeText(context,"bb",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SaloonViewModel::class.java)
        // TODO: Use the ViewModel
    }

}