package com.example.scb12.appeventos.fragments


import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v7.app.ActionBarDrawerToggle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.scb12.appeventos.MainActivity

import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.databinding.FragmentEventDetailBinding
import com.example.scb12.appeventos.entities.Event
import com.example.scb12.appeventos.entities.Location
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EventDetailFragment : Fragment() {

    lateinit var binding: FragmentEventDetailBinding private set
    lateinit var activity: MainActivity private set
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var eventGson: String
    private lateinit var event: Event
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var locationUrl: String
    private lateinit var location: Location
    private lateinit var menu: Menu

        companion object {
        fun newInstance(param1: String) =
                EventDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        compositeDisposable = CompositeDisposable()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false)
        activity = getActivity() as MainActivity


        eventGson = arguments!!.getString(ARG_PARAM1)!!

        val gson = Gson()
        event = gson.fromJson(eventGson, Event::class.java)
        locationUrl = "https://www.eventbriteapi.com/v3/venues/" + event.venueId + "/?token=EGCNBKQRZWJAAPOFDFVJ&"
        loadLocation()
        loadImage(event.logoUrl)


        //BINDINGS

        binding.tvName.text = event.name
        binding.tvDescription.text = event.description

        val startDate = event.startDate.substring(0, event.startDate.length - 4).replace("T", "  ")
        val finishDate = event.finishDate.substring(0, event.finishDate.length - 4).replace("T", "  ")

        binding.tvStartDate.text = startDate
        binding.tvFinishDate.text = finishDate

        /*binding.tvLocation.text = location.city
        binding.tvLocation.append(", ")
        binding.tvLocation.append(location.country)

        binding.tvAddress.text = location.address*/






        activity.setTitle(R.string.events)

        activity.setSupportActionBar(binding.tb)
        val actionBar = activity.supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        activity.binding.nv.menu.findItem(R.id.nav_events).isChecked = true

        drawerToggle = ActionBarDrawerToggle(activity, activity.binding.dl,
            binding.tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        activity.binding.dl.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        activity.binding.nv.menu.findItem(R.id.nav_events).isChecked = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        activity.binding.nv.menu.findItem(R.id.nav_events).isChecked = false
        compositeDisposable.dispose()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (::drawerToggle.isInitialized) {
            drawerToggle.onConfigurationChanged(newConfig)
        }
    }

    private fun loadLocation() {
        val requestQueue = Volley.newRequestQueue(activity)
        val objectRequest = JsonObjectRequest(Request.Method.GET, locationUrl,
            null, Response.Listener<JSONObject> { response ->
                val jsonObject = response?.getJSONObject("address")
                location = Location(
                    jsonObject!!.getString("city"),
                    jsonObject.getString("country"),
                    jsonObject.getString("latitude"),
                    jsonObject.getString("longitude"),
                    jsonObject.getString("region"),
                    jsonObject.getString("localized_address_display")
                )

                binding.tvLocation.text = location.city
                binding.tvLocation.append(", ")
                binding.tvLocation.append(location.country)

                binding.tvAddress.text = location.address
                binding.tvAddress.movementMethod = ScrollingMovementMethod()
            }, Response.ErrorListener { Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show() })
        requestQueue.add(objectRequest)

    }

    // TODO: ESTO TIENE QUE IR EN OTRO HILO

   private fun loadImage(url: String) {
            doAsync{
                val imageUrl = URL(url)
                val conn: HttpURLConnection = imageUrl.openConnection() as HttpURLConnection
                conn.connect()
                val image = BitmapFactory.decodeStream(conn.inputStream)
                uiThread {
                    binding.imageView.setImageBitmap(image)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_event_detail, menu)
        this.menu = menu!!
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val favChecked = menu.findItem(R.id.favChecked)
        val favNotChecked = menu.findItem(R.id.fav)
        when(item!!.itemId) {
            R.id.fav -> {
                favNotChecked.isVisible = false
                favChecked.isVisible = true
                //TODO: AÑADIR A FAVORITOS
            }

            R.id.favChecked -> {
                favChecked.isVisible = false
                favNotChecked.isVisible = true
                //TODO: ELIMINAR DE FAVORITOS
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
