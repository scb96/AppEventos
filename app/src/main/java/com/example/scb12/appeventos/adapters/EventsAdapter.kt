package com.example.scb12.appeventos.adapters

import android.content.res.Resources
import android.graphics.Color
import android.provider.Settings.Global.getString
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.databinding.EventRowBinding
import com.example.scb12.appeventos.entities.Event
import com.example.scb12.appeventos.fragments.EventDetailFragment
import com.example.scb12.appeventos.fragments.EventsFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.event_row.view.*
import org.jetbrains.anko.backgroundColor
import kotlin.collections.ArrayList


class EventsAdapter(
    val fragment: EventsFragment,
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
        notifyItemRangeRemoved(start, count)
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
        val holder = holder as EventViewHolder
        val event = items[position]
        val binding = holder.binding
        //holder.tvName.text = items[position]
        binding.tvName.text = event.name
        binding.tvName.movementMethod = ScrollingMovementMethod()

        val date = event.startDate.substring(0, event.startDate.length - 4).replace("T", "  ")

        binding.tvDate.text = date
        binding.tvDate.append("h")

        if (event.isFree == "true") {
            binding.tvGenre.text = " FREE "//Resources.getSystem().getString(R.string.free)
            binding.tvGenre.background = ContextCompat.getDrawable(fragment.activity, R.drawable.free)
            //holder.tvGenre.backgroundColor = Color.GREEN
            //holder.tvGenre.setBackgroundColor(Color.GREEN)
        } else { binding.tvGenre.text = ""
         /*   holder.tvGenre.text = "NOT FREE"//Resources.getSystem().getString(R.string.not_free)
            holder.tvGenre.background = ContextCompat.getDrawable(fragment.activity, R.drawable.not_free)*/
//            holder.tvGenre.setBackgroundColor(Color.RED)
            //holder.tvGenre.backgroundColor = Color.RED

        }


        val img = binding.ivFav
        val imgC = binding.ivFavChecked
        img.setOnClickListener {
            img.visibility = View.INVISIBLE
            imgC.visibility = View.VISIBLE
        }

        imgC.setOnClickListener {
            imgC.visibility = View.INVISIBLE
            img.visibility = View.VISIBLE
        }

        if(!binding.root.hasOnClickListeners()) {
            binding.root.setOnClickListener {
                val clickedPosition = holder.adapterPosition
                val clickedItem = items[clickedPosition]

                val gson = Gson()
                val eventGson = gson.toJson(clickedItem)

                val eventDetailFragment = EventDetailFragment.newInstance(eventGson)

                fragment.fragmentManager!!.beginTransaction()
                    .replace(R.id.clMain, eventDetailFragment)
                    .addToBackStack(null)
                    .commit()

            }
        }
    }

    fun filter(text: String) {
        var text = text
        items.clear()
        if (text.isEmpty()) {
            items.addAll(rowItemsCopy)
        } else {
            text = text.toLowerCase()
            for (item in rowItemsCopy) { //FILTRO POR NOMBRE
                if (item.name.toLowerCase().contains(text)) {
                    items.add(item)
                }
                //FILTRO POR ARTISTA
            }

            notifyDataSetChanged()
        }
    }

}
   /* class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.tvName
        val tvDate = view.tvDate
        val tvGenre = view.tvGenre
        val ivFav = view.ivFav
        val ivFavC = view.ivFavChecked
    }*/
class EventViewHolder(val binding: EventRowBinding) : RecyclerView.ViewHolder(binding.root)
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

