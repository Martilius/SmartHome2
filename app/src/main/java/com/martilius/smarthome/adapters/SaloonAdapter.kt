package com.martilius.smarthome.adapters

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings
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
import com.martilius.smarthome.models.Cupboard
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.item_on_off.view.*
import kotlinx.coroutines.*
import java.lang.Exception

class SaloonAdapter(
    private val listener: (Cupboard) -> Unit
) : ListAdapter<Cupboard, SaloonAdapter.SaloonViewHolder>(DIFF_CALLBACK) {


    class SaloonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val button: ToggleButton = itemView.findViewById(R.id.toggleButtonItemOnOff)
        fun bind(item: Cupboard, listener: (Cupboard) -> Unit) {
            itemView.apply {
                tv_item_on_off.text = item.name
                setOnClickListener { listener(item) }

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
        getItem(position).message
        val udpServices = UdpServices()
        var finished = false
        val synchronizedObject = Object()
        udpServices.sendWithResult(
            "startup;${getItem(position).message}",
            holder.itemView.context
        )


        GlobalScope.launch(Dispatchers.Main) {
            with(udpServices) {
                result.observe(holder.itemView.context as LifecycleOwner, Observer {
                    val parts: List<String> = it.split(";")
                    if (parts[3].equals("on")) {
                        holder.itemView.toggleButtonItemOnOff.isChecked = true
                    }
                    finished = true
                })
            }
            delay(50)
        }


    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Cupboard>() {
            override fun areItemsTheSame(oldItem: Cupboard, newItem: Cupboard) =
                oldItem.name == newItem.name

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Cupboard, newItem: Cupboard) =
                oldItem == newItem
        }
    }

}