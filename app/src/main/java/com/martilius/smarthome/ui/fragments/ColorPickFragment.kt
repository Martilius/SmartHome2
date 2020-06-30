package com.martilius.smarthome.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.DialogFragment
import com.google.android.material.transition.MaterialFadeThrough
import com.martilius.smarthome.R
import com.martilius.smarthome.ui.viewmodels.ColorPickViewModel
import kotlinx.android.synthetic.main.color_pick_fragment.view.*


class ColorPickFragment : DialogFragment() {

    companion object {
        fun newInstance() = ColorPickFragment()
    }

    private lateinit var viewModel: ColorPickViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.color_pick_fragment, container, false).apply {
            var hsv = FloatArray(3)
            var color = 0
            enterTransition = MaterialFadeThrough().setDuration(300L)
            exitTransition = MaterialFadeThrough().setDuration(300L)
            val bundle = arguments
            if(bundle!=null){
                color = Color.parseColor(bundle.getString("customButtonColor").toString())
                Color.colorToHSV(color,hsv)
            }


            btcancel.setOnClickListener {
                dismiss()
            }
        }
    }

}