package com.example.scb12.appeventos.adapters

import android.support.v7.app.AlertDialog
import android.support.v7.widget.AlertDialogLayout
import android.support.v7.widget.RecyclerView
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.databinding.EventRowBinding
import com.example.scb12.appeventos.entities.Event
import com.example.scb12.appeventos.fragments.MyEventsFragment
import org.jetbrains.anko.onLongClick

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

    fun addItem(event: Event) {
        this.rowItems.add(event)
        this.rowItemsCopy.add(event)
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

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val position = holder.adapterPosition
        val holder = holder
        val event = items[position]
        val binding = holder.binding

        binding.tvName.text = event.name
        binding.tvName.movementMethod = ScrollingMovementMethod()

        //val date = event.startDate.substring(0, event.startDate.length - 10)//.replace("T", "  ")

        binding.tvDate.text = event.startDate

        if (event.isFree == "true") {
            binding.ivFree.visibility = View.VISIBLE
        } else {
            binding.ivFree.visibility = View.GONE
        }

        binding.bFav.isChecked = event.isFav

        binding.root.setOnLongClickListener {
            val clickedPosition = holder.adapterPosition
            val clickedItem= items[clickedPosition]

            val builder = AlertDialog.Builder(fragment.activity)
                .setPositiveButton("YES") {_, _ ->
                    items.remove(clickedItem)
                    notifyDataSetChanged()
                }
                .setNegativeButton("NO") {dialog, _ ->
                    dialog.cancel()
                }
                .setMessage(R.string.sure)

            val dialog = builder.create()
            dialog.show()
            true
        }
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
