package com.example.scb12.appeventos.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.fragments.EventsFragment
import kotlinx.android.synthetic.main.event_row.view.*
import kotlin.collections.ArrayList


class EventsAdapter(
    val fragment: EventsFragment,
    var items: ArrayList<String>
) : RecyclerView.Adapter<EventViewHolder>() {

    var rowItems: ArrayList<String> = ArrayList()
    var rowItemsCopy: ArrayList<String> = ArrayList(
        items.filterIsInstance<String>()
    )

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItems(rowItems: ArrayList<String>) {
        this.rowItems.addAll(rowItems)
        this.rowItemsCopy.addAll(rowItems)
    }

    fun clear() {
        rowItems.clear()
        rowItemsCopy.clear()
        val start = 0
        val count = itemCount
        notifyItemRangeRemoved(start, count)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(fragment.context).inflate(R.layout.event_row, parent, false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {

        holder.tvName.text = items[position]

         val img = holder.ivFav
         val imgC = holder.ivFavC
        img.setOnClickListener {
            img.visibility = View.INVISIBLE
            imgC.visibility = View.VISIBLE
        }

        imgC.setOnClickListener {
            imgC.visibility = View.INVISIBLE
            img.visibility = View.VISIBLE
        }
    }

    fun filter(text: String) {
        var text = text
        items.clear()
        if(text.isEmpty()) {
            items.addAll(rowItemsCopy)
        } else {
            text = text.toLowerCase()
            for(item in rowItemsCopy) { //FILTRO POR NOMBRE
                if(item.toLowerCase().contains(text)) {
                    items.add(item)
                }
                //FILTRO POR ARTISTA
            }
        }
        notifyDataSetChanged()
    }
}


class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.tvName
    val ivFav = view.ivFav
    val ivFavC = view.ivFavChecked
/*
class EventsAdapter(
    val fragment: EventsFragment,
    var items: ArrayList<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TAG: String = "EventsAdapter"
    }

    private var rowItems: ArrayList<String> = ArrayList()

    val rowItemCount: Int get() = rowItems.size

    fun addRowItems(rowItems: ArrayList<String>) {
        this.rowItems.addAll(rowItems)
        addAll(rowItems.toMutableList<Any>() as ArrayList<Any>)
    }

    fun clear() {
        rowItems.clear()
        val start = 0
        val count = itemCount
        notifyItemRangeRemoved(start, count)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
                val binding = EventRowBinding.inflate(
                    inflater,
                    p0,
                    false
                )
                return EventsViewHolder(binding)

    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val position = p0.adapterPosition
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
               img.setImageDrawable(Drawable.createFromPath("@drawable/ic_star_checked"))
           }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }*/
}