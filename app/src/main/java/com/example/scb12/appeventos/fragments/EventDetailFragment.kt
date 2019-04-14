package com.example.scb12.appeventos.fragments


import android.content.DialogInterface
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getDrawable
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Button
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_map.*
import kotlinx.android.synthetic.main.dialog_map.view.*
import kotlinx.android.synthetic.main.fragment_event_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
//import android.arch.lifecycle

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EventDetailFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentEventDetailBinding private set
    lateinit var activity: MainActivity private set
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var eventGson: String
    private lateinit var event: Event
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var locationUrl: String
    private lateinit var location: Location
    private lateinit var menu: Menu
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap


    var favsList: ArrayList<Event> = ArrayList()

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
        val gson = Gson()
        activity = getActivity() as MainActivity
        eventGson = arguments!!.getString(ARG_PARAM1)!!
        event = gson.fromJson(eventGson, Event::class.java)
        locationUrl = "https://www.eventbriteapi.com/v3/venues/" + event.venueId + "/?token=EGCNBKQRZWJAAPOFDFVJ&"

        loadLocation()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        compositeDisposable = CompositeDisposable()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false)
        activity = getActivity() as MainActivity
        loadImage(event.logoUrl)
        val dialogView = layoutInflater.inflate(R.layout.dialog_map, null)

        mapView = dialogView.findViewById(R.id.map)
        mapView.getMapAsync(this)

        binding.bMap.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.cancel()
            }
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
        }

        //BINDINGS

        binding.tvName.text = event.name
        binding.tvDescription.text = event.description

        val startDate = event.startDate.substring(0, event.startDate.length - 4).replace("T", "  ")
        val finishDate = event.finishDate.substring(0, event.finishDate.length - 4).replace("T", "  ")

        binding.tvStartDate.text = startDate
        binding.tvFinishDate.text = finishDate
//
//        val favsFrag = FavsFragment()
//        var bundle = Bundle()
//        bundle.putParcelableArrayList("favsList", favsList)

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

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(0.0, 0.0)))
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
            }, Response.ErrorListener { })
        requestQueue.add(objectRequest)

    }

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
        if(event.isFav) {
            menu.findItem(R.id.favChecked).isVisible = true
            menu.findItem(R.id.fav).isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val favChecked = menu.findItem(R.id.favChecked)
        val favNotChecked = menu.findItem(R.id.fav)
        when(item!!.itemId) {
            R.id.fav -> {
                favNotChecked.isVisible = false
                favChecked.isVisible = true
                event.isFav = true
                activity.addFav(event)
            }

            R.id.favChecked -> {
                favChecked.isVisible = false
                favNotChecked.isVisible = true
                event.isFav = false
                activity.removeFav(event)
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun startFavsFragment(){
//        val listFavsFragment = FavsFragment.newInstance()
//        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.clMain, listFavsFragment)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
//    }
}
