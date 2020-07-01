package com.martilius.smarthome.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martilius.smarthome.R
import com.martilius.smarthome.models.Cupboard
import kotlinx.android.synthetic.main.item_on_off.view.*

class SaloonAdapter (private val listener: (Cupboard) -> Unit
): ListAdapter<Cupboard, SaloonAdapter.SaloonViewHolder>(DIFF_CALLBACK){


    class SaloonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:Cupboard, listener: (Cupboard) -> Unit){
            itemView.apply {
                tv_item_on_off.text = item.name

                setOnClickListener { listener(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SaloonViewHolder ( LayoutInflater.from(parent.context).inflate(
        R.layout.item_on_off, parent, false)

    )

    override fun onBindViewHolder(holder: SaloonViewHolder, position: Int) =
        holder.bind(getItem(position), listener)


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