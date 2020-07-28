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
import com.martilius.smarthome.Tasks.UdpServices
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.models.Cupboard
import com.martilius.smarthome.models.NewDevice
import com.martilius.smarthome.repository.remote.ConfigurationService
import kotlinx.android.synthetic.main.item_add_device_info.view.*
import kotlinx.android.synthetic.main.item_on_off.view.*
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewDeviceAdapter(
    private val listener: (NewDevice) -> Unit
) : ListAdapter<NewDevice, NewDeviceAdapter.NewDeviceViewHolder>(DIFF_CALLBACK) {

    var selectedPosition = -1
    lateinit var selectedDevice:NewDevice


    inner class NewDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: NewDevice, listener: (NewDevice) -> Unit) {
            itemView.apply {
                tvItemAddDevice.text = item.ip
                setOnClickListener { listener(item) }
//                if(ivItemAddDevice.visibility.equals(View.GONE)){
//                    ivItemAddDevice.visibility= View.VISIBLE
//                }else{
//                    ivItemAddDevice.visibility= View.GONE
//                }
                if (selectedPosition == -1) {
                    ivItemAddDevice.visibility = View.GONE
                } else {
                    if (selectedPosition.equals(adapterPosition)) {
                        ivItemAddDevice.visibility = View.VISIBLE
                        selectedDevice = item
                    } else {
                        ivItemAddDevice.visibility = View.GONE
                    }
                }
                item_add_info_layout.setOnClickListener {
                    if (selectedPosition != adapterPosition) {
                        notifyItemChanged(selectedPosition)
                        selectedPosition = adapterPosition
                        notifyItemChanged(selectedPosition)
                    }else{
                        ivItemAddDevice.visibility = View.GONE
                        selectedPosition=-1
                        notifyItemChanged(selectedPosition)
                    }
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewDeviceViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_add_device_info, parent, false
        ).apply {
        }

        )

    override fun onBindViewHolder(holder: NewDeviceViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
        if (selectedPosition.equals(position)) {
            holder.itemView.ivItemAddDevice.visibility = View.VISIBLE

        } else {
            holder.itemView.ivItemAddDevice.visibility = View.GONE
        }

//        holder.itemView.item_add_info_layout.setOnClickListener {
//            if (selectedPosition != position) {
//                notifyItemChanged(selectedPosition)
//                selectedPosition = position
//                notifyItemChanged(selectedPosition)
//            }else{
//
//                selectedPosition=-1
//                notifyItemChanged(selectedPosition)
//            }
//        }

    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewDevice>() {
            override fun areItemsTheSame(oldItem: NewDevice, newItem: NewDevice) =
                oldItem.ip == newItem.ip

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: NewDevice, newItem: NewDevice) =
                oldItem == newItem
        }
    }
    public fun getSelected():Int{
        return selectedPosition
    }

}