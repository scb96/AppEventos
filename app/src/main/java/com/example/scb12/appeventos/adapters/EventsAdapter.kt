package com.example.scb12.appeventos.adapters

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Color
import android.provider.Settings.Global.getString
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.volley.toolbox.Volley
import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.databinding.EventRowBinding
import com.example.scb12.appeventos.entities.Category
import com.example.scb12.appeventos.entities.Event
import com.example.scb12.appeventos.fragments.EventDetailFragment
import com.example.scb12.appeventos.fragments.EventsFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.event_row.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList


class EventsAdapter(
    val fragment: EventsFragment,
    var items: ArrayList<Event>
) : RecyclerView.Adapter<EventViewHolder>() {

    var rowItems: ArrayList<Event> = ArrayList()
    var rowItemsCopy: ArrayList<Event> = ArrayList(
        items.filterIsInstance<Event>()
    )

    private var category: Category? = null

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

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val position = holder.adapterPosition
        val holder = holder as EventViewHolder
        val event = items[position]
        val binding = holder.binding
        //holder.tvName.text = items[position]
        binding.tvName.text = event.name
        binding.tvName.movementMethod = ScrollingMovementMethod()

        val date = event.startDate.substring(0, event.startDate.length - 10)//.replace("T", "  ")

        binding.tvDate.text = date
        //binding.tvDate.append("h")

        if (event.isFree == "true") {
            binding.ivFree.visibility = View.VISIBLE
            //binding.tvGenre.text = " FREE "//Resources.getSystem().getString(R.string.free)
            // binding.tvGenre.background = ContextCompat.getDrawable(fragment.activity, R.drawable.free)
            //holder.tvGenre.backgroundColor = Color.GREEN
            //holder.tvGenre.setBackgroundColor(Color.GREEN)
        } else {
            binding.ivFree.visibility = View.GONE
        }

            // binding.tvGenre.text = ""
            /*   holder.tvGenre.text = "NOT FREE"//Resources.getSystem().getString(R.string.not_free)
            holder.tvGenre.background = ContextCompat.getDrawable(fragment.activity, R.drawable.not_free)*/
//            holder.tvGenre.setBackgroundColor(Color.RED)
            //holder.tvGenre.backgroundColor = Color.RED

        event.isFav = binding.bFav.isChecked

        binding.bFav.setOnClickListener {
            if(binding.bFav.isChecked) {
                event.isFav = false
                // TODO: SE BORRA DE FAVORITOS

            } else {
                event.isFav = true
                //TODO: SE AÑADE A FAVORITOS
            }
        }

        loadImage(event.logoUrl, binding.ivEvent)
        fragment.getCategory(event.categoryId)

        binding.tvGenre.text = category?.name


        /*  val img = binding.ivFav
        val imgC = binding.ivFavChecked
        img.setOnClickListener {
            img.visibility = View.INVISIBLE
            imgC.visibility = View.VISIBLE
        }

        imgC.setOnClickListener {
            imgC.visibility = View.INVISIBLE
            img.visibility = View.VISIBLE
        }*/

        if (!binding.root.hasOnClickListeners()) {
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
            for (item in rowItemsCopy) {
                if (item.name.toLowerCase().contains(text)) {
                    items.add(item)
                }
            }

            notifyDataSetChanged()
        }
    }

    private fun loadImage(url: String, image: ImageView) {
        doAsync {
            val imageUrl = URL(url)
            val conn: HttpURLConnection = imageUrl.openConnection() as HttpURLConnection
            conn.connect()
            val bitmap = BitmapFactory.decodeStream(conn.inputStream)
            uiThread {
                image.setImageBitmap(bitmap)
            }
        }
    }

    fun addCategory(category: Category) {
        this.category = category
    }
}

class EventViewHolder(val binding: EventRowBinding) : RecyclerView.ViewHolder(binding.root)

