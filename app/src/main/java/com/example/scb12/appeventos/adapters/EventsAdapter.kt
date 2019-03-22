package com.example.scb12.appeventos.adapters

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.databinding.EventRowBinding
import com.example.scb12.appeventos.fragments.EventsFragment
import java.util.*
import java.util.Collections.addAll

class EventsAdapter(
    val fragment: EventsFragment,
    var items: ArrayList<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TAG: String = "EventsAdapter"
        private const val VIEW_TYPE_ITEM = 0
    }

    private var rowItems: ArrayList<String> = ArrayList()

    val rowItemCount: Int get() = rowItems.size

    fun addRowItems(rowItems: ArrayList<String>) {
        this.rowItems.addAll(rowItems)
        addAll(rowItems.toMutableList<Any>() as ArrayList<Any>)
    }

    fun clear() {
        rowItems.clear()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        when(p1) {
            VIEW_TYPE_ITEM -> {
                val binding = EventRowBinding.inflate(
                    inflater,
                    p0,
                    false
                )
                return EventsViewHolder(binding)
            }
        }
        return super.createViewHolder(p0, p1)
    }

    override fun getItemViewType(position: Int): Int {
        val o = items[position]
        if(o is String) {
            return VIEW_TYPE_ITEM
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val position = p0.adapterPosition
        when(getItemViewType(p1)) {
            VIEW_TYPE_ITEM -> {
                val holder = p0 as EventsViewHolder
                val event = items[position] as String
                val binding = holder.binding

                binding.tvName.text = event
                binding.tvDate.text = "22/03/2019"
                binding.tvGenre.text = "Género"

                binding.ivFav.setOnClickListener {
                    val img = binding.ivFav
                    if(img.drawable == Drawable.createFromPath("@drawable/ic_star_checked")) {
                        img.setImageDrawable(Drawable.createFromPath("@drawable/ic_star_not_checked"))
                    }

                    if(img.drawable == Drawable.createFromPath("@drawable/ic_star_not_checked")) {
                        img.setImageDrawable(Drawable.createFromPath("@drawable/ic_star_" +
                                "checked"))
                    }


                }

                }
            }
        }

    override fun getItemCount(): Int {
        return items.size
    }
}