package com.example.scb12.appeventos.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.scb12.appeventos.databinding.EventRowBinding
import com.example.scb12.appeventos.entities.Event
import com.example.scb12.appeventos.fragments.MyEventsFragment

class MyEventsAdapter(
    val fragment: MyEventsFragment,
    var items: ArrayList<Event>
) : RecyclerView.Adapter<EventViewHolder>() {

    var rowItems: ArrayList<Event> = ArrayList()
    var rowItemsCopy: ArrayList<Event> = ArrayList(
        items.filterIsInstance<Event>()
    )

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItems(rowItems: ArrayList<Event>) {
        this.rowItems.addAll(rowItems)
        this.rowItemsCopy.addAll(rowItems)
    }

    fun clear() {
        rowItems.clear()
        rowItemsCopy.clear()
        val start = 0
        val count = itemCount
        notifyDataSetChanged()
        //notifyItemRangeRemoved(start, count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventRowBinding.inflate(
            inflater,
            parent,
            false
        )
        return EventViewHolder(binding)
        //return EventViewHolder(LayoutInflater.from(fragment.context).inflate(R.layout.event_row, parent, false))
    }

    override fun onBindViewHolder(p0: EventViewHolder, p1: Int) {

    }

    fun filter(text: String) {
        var text = text
        items.clear()
        if (text.isEmpty()) {
            items.addAll(rowItemsCopy)
        } else {
            text = text.toLowerCase()
            for (item in rowItemsCopy) {
                if (item.name.toLowerCase().contains(text)) {
                    items.add(item)
                }
            }

            notifyDataSetChanged()
        }
    }

}