package com.martilius.smarthome.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martilius.smarthome.R
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.repository.remote.ConfigurationService
import kotlinx.android.synthetic.main.item_headlight.view.*
import kotlinx.android.synthetic.main.item_led_rgb.view.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HeadLightAdapter(
    private val listener: (Configuration) -> Unit
) : ListAdapter<Configuration, HeadLightAdapter.HeadLightViewHolder>(DIFF_CALLBACK) {


    class HeadLightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.2.174:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(ConfigurationService::class.java)

        //val button: ToggleButton = itemView.findViewById(R.id.toggleButtonItemOnOff)
        fun bind(item: Configuration, listener: (Configuration) -> Unit) {
            itemView.apply {
                tvHeadLightTitle.text = item.name
                btHeadLightSwitch.isChecked = item.state.equals("on")
                if(item.state.equals("on")){
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2on)
                }else{
                    ivHeadLightCardView.setImageResource(R.drawable.lampv2off)
                }



                btHeadLightSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if (isChecked && (btHeadLightSwitch.isPressed || headerHeadLight.isPressed)) {
                        ivHeadLightCardView.setImageResource(R.drawable.lampv2on)
                        GlobalScope.launch {
                            retrofit.changeState(item.ip, "on")
                        }
                    } else if (!isChecked && (btHeadLightSwitch.isPressed || headerHeadLight.isPressed)) {
                        ivHeadLightCardView.setImageResource(R.drawable.lampv2off)
                        GlobalScope.launch {
                            retrofit.changeState(item.ip, "off")
                        }
                    }

                }
                //setOnClickListener { listener(item) }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HeadLightViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_headlight, parent, false
        ).apply {

            cvHeadLight.setOnClickListener {
                btHeadLightSwitch.isChecked = btHeadLightSwitch.isChecked != true
            }
        }
        )

    override fun onBindViewHolder(holder: HeadLightViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.2.174:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()
            .create(ConfigurationService::class.java)
        val bla = getItem(position)

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