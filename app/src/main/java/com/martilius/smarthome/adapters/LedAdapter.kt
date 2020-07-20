package com.martilius.smarthome.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martilius.smarthome.R
import com.martilius.smarthome.Tasks.UdpServices
import com.martilius.smarthome.di.modules.RepositoryModule_ProvideRepositoryFactory
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.models.Cupboard
import com.martilius.smarthome.repository.Repository
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.item_led_rgb.view.*
import kotlinx.android.synthetic.main.item_on_off.view.*
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class LedAdapter(
    private val listener: (Configuration)->Unit
) : ListAdapter<Configuration, LedAdapter.LedViewHolder>(DIFF_CALLBACK) {




    class LedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val button: ToggleButton = itemView.findViewById(R.id.toggleButtonItemOnOff)
        fun bind(item: Configuration, listener: (Configuration) -> Unit) {
            itemView.apply {
                tvLedRGBTitle.text = item.name
                btAdditionalLightSwitch.isChecked = item.state.equals("on")
                additionalLightCustomButton.backgroundTintList = ColorStateList.valueOf(Color.rgb(item.red, item.green, item.blue))
                ivAdditionalLightCardView.setBackgroundColor(Color.rgb(item.red, item.green, item.blue))
                btAdditionalLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if (isChecked && btAdditionalLightSwitch.isPressed){
                        GlobalScope.launch {
                            //repository.changeState(item.ip,"on")
                        }
                    } else if(!isChecked && btAdditionalLightSwitch.isPressed){
                        GlobalScope.launch {
                            //repository.changeState(item.ip,"off")
                        }
                    }
                }
                setOnClickListener { listener(item) }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LedViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_led_rgb, parent, false
        ).apply {

            headerAdditionalLight.setOnClickListener{
                btAdditionalLightCardViewExpander.isChecked = btAdditionalLightCardViewExpander.isChecked != true
            }
            btAdditionalLightCardViewExpander.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    TransitionManager.beginDelayedTransition(parent,AutoTransition())
                    contentAdditionalLightCardView.visibility = View.VISIBLE
                } else {
                    TransitionManager.beginDelayedTransition(parent,AutoTransition())
                    contentAdditionalLightCardView.visibility = View.GONE

                }
            }

//            btAdditionalLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
//                if (isChecked && btAdditionalLightSwitch.isPressed){
//                    GlobalScope.launch(Dispatchers.IO) {
//                        repository.changeState("4444","on")
//                    }
//                } else if(!isChecked && btAdditionalLightSwitch.isPressed){
//                    GlobalScope.launch(Dispatchers.IO) {
//                        repository.changeState("4444","off")
//                    }
//                }
//            }


        }
        )

    override fun onBindViewHolder(holder: LedViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Configuration>() {
            override fun areItemsTheSame(oldItem: Configuration, newItem: Configuration) =
                oldItem.name == newItem.name

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Configuration, newItem: Configuration) =
                oldItem == newItem
        }
    }

}