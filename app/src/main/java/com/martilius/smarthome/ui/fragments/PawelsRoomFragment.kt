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
            additionalLightCustomButton.backgroundTintList =ColorStateList.valueOf(sharedPreferences?.getString("PawelsRoomCustomColor","#FFFFFF").toString().toInt())

            headerAdditionalLight.setOnClickListener{
                btAdditionalLightCardViewExpander.isChecked = btAdditionalLightCardViewExpander.isChecked != true
            }

            btPickCustomColorPawelsRoom.setOnClickListener {
//                var bundle:Bundle = bundleOf("customButtonColor" to sharedPreferences?.getString("PawelsRoomCustomColor","#FFFFFF"))
//                val colorFragment = ColorPickFragment()
//                colorFragment.arguments = bundle
//                childFragmentManager.beginTransaction().add(colorFragment, "colorFragment").commit()
                if (sharedPreferences != null) {
                    //val colorpickdial = ColorPickDialog()
                    ColorPickDialog().showDialog(context,sharedPreferences,"PawelsRoomCustomColor",additionalLightCustomButton)
                    //showDialog(context, sharedPreferences,"PawelsRoomCustomColor")
                }
                //ColorPickFragment().show(childFragmentManager,"b")

            }

            srlPawelsRoom.setOnRefreshListener {
                srlPawelsRoom.isRefreshing = false
            }


            additionalLightWhiteButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.WHITE)
            sharedPreferences?.edit()?.putString("PawelsRoomCustomColor",Color.RED.toString())?.apply()}
            additionalLightRedButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.RED) }
            additionalLightGreenButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.GREEN) }
            additionalLightBlueButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(Color.BLUE) }
            additionalLightCustomButton.setOnClickListener { ivAdditionalLightCardView.setBackgroundColor(sharedPreferences?.getString("PawelsRoomCustomColor","#FFFFFF").toString().toInt()) }

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

    fun showDialog(context: Context, sharedPreferences:SharedPreferences, sharedprefsID:String) {
        val dialog = Dialog(context)
        var hsv = FloatArray(3)
        var hsl = FloatArray(3)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.color_pick_fragment)
        val okBtn = dialog.findViewById(R.id.btok) as Button
        val cancelBtn = dialog.findViewById(R.id.btcancel) as Button
        val sliderValue = dialog.findViewById(R.id.sliderValueColorPicker) as Slider
        val sliderBrightness = dialog.findViewById(R.id.sliderBrightnessColorPicker) as Slider
        val colorPicker = dialog.findViewById(R.id.colorPicker) as ColorPickerView
        val tv = dialog.findViewById(R.id.tvVal) as TextView
        val tvValue = dialog.findViewById(R.id.tvColorValue) as TextView
        val tvBrightness = dialog.findViewById(R.id.tvColorBrightness) as TextView
        var color = sharedPreferences?.getString(sharedprefsID,"#FFFFFF").toString().toInt()
        colorPicker.setBackgroundColor(color)
        colorPicker.setColor(color)
        Color.colorToHSV(color,hsv)
        ColorUtils.colorToHSL(color,hsl)

        sliderValue.value= (hsv[2] * 100)
        tvValue.text = "Color value: ${(hsv[2]*100).toInt()}%"
        sliderBrightness.value = (hsl[2]*100)
        tvBrightness.text = "Color brightness: ${(hsl[2]*100).toInt()}%"

        okBtn.setOnClickListener {
            additionalLightCustomButton.backgroundTintList =ColorStateList.valueOf(sharedPreferences.getString(sharedprefsID,"#FFFFFF").toString().toInt())
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener{
            additionalLightCustomButton.backgroundTintList =ColorStateList.valueOf(sharedPreferences.getString(sharedprefsID,"#FFFFFF").toString().toInt())
            dialog.dismiss()
        }

        colorPicker.setColorListener { pickedColor, s ->
            colorPicker.setBackgroundColor(pickedColor)
            sharedPreferences.edit().putString(sharedprefsID, pickedColor.toString()).apply()
            Color.colorToHSV(pickedColor,hsv)
            sliderValue.value= (hsv[2] * 100)
            tv.text = hsv[2].toString()
            tvValue.text = "Color value: ${(hsv[2]*100).toInt()}%"
            ColorUtils.colorToHSL(pickedColor,hsl)
            tvBrightness.text = "Color brightness: ${(hsl[2]*100).toInt()}%"
            sliderBrightness.value = hsl[2]*100
        }

        sliderValue.addOnChangeListener { slider, value, fromUser ->
            if(fromUser) {
                tvValue.text = "Color value: ${value.toInt()}%"
                hsv[2]=value/100
                color=Color.HSVToColor(hsv)
                //color=ColorUtils.HSLToColor(hsl)
                colorPicker.setColor(color)
                colorPicker.setBackgroundColor(color)
                ColorUtils.colorToHSL(color,hsl)
                tvBrightness.text = "Color brightness: ${(hsl[2]*100).toInt()}%"
                sliderBrightness.value = hsl[2]*100
                sharedPreferences.edit().putString(sharedprefsID, color.toString()).apply()
            }
        }

        sliderBrightness.addOnChangeListener { slider, value, fromUser ->
            if(fromUser) {
                tvBrightness.text = "Color brightness: ${value.toInt()}%"
                hsl[2] = value / 100
                color = ColorUtils.HSLToColor(hsl)
                colorPicker.setColor(color)
                colorPicker.setBackgroundColor(color)
                Color.colorToHSV(color,hsv)
                tvValue.text = "Color value: ${(hsv[2]*100).toInt()}%"
                sliderValue.value = hsv[2]*100
                sharedPreferences.edit().putString(sharedprefsID, color.toString()).apply()
            }
        }

        if(dialog.window!=null) dialog.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
        dialog.show()
    }

}