package com.martilius.smarthome.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martilius.smarthome.R
import com.martilius.smarthome.Service.StompService
import com.martilius.smarthome.Tasks.UdpServices
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.models.Cupboard
import com.martilius.smarthome.repository.remote.ConfigurationService
import kotlinx.android.synthetic.main.item_on_off.view.*
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OnOffAdapter(
    private val listener: (Configuration) -> Unit
) : ListAdapter<Configuration, OnOffAdapter.SaloonViewHolder>(DIFF_CALLBACK) {

    val stomp = StompService()

    inner class SaloonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val button: ToggleButton = itemView.findViewById(R.id.toggleButtonItemOnOff)
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.2.174:8080/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(OkHttpClient())
//            .build()
//            .create(ConfigurationService::class.java)

        fun bind(item: Configuration, listener: (Configuration) -> Unit) {
            itemView.apply {
                stomp.initial()
                tv_item_on_off.text = item.name
                toggleButtonItemOnOff.isChecked = item.state.equals("on")

                item_on_off.setOnClickListener {
                    toggleButtonItemOnOff.isChecked = toggleButtonItemOnOff.isChecked != true

                }
                toggleButtonItemOnOff.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if(isChecked && (toggleButtonItemOnOff.isPressed || item_on_off.isPressed)){
                        stomp.sendMessage("/device/${item.ip}/${item.red}/${item.green}/${item.blue}/on")
                        }else if(!isChecked && (toggleButtonItemOnOff.isPressed || item_on_off.isPressed)){
                        stomp.sendMessage("/device/${item.ip}/${item.red}/${item.green}/${item.blue}/off")
                    }
                }
                //setOnClickListener { listener(item) }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SaloonViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_on_off, parent, false
        ).apply {

        }

        )

    override fun onBindViewHolder(holder: SaloonViewHolder, position: Int) {
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