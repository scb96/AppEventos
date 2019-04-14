package com.example.scb12.appeventos.fragments


import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Contacts
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.*
import android.text.TextUtils.indexOf
import android.view.*
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.scb12.appeventos.MainActivity

import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.adapters.EventsAdapter
import com.example.scb12.appeventos.databinding.FragmentEventsBinding
import com.example.scb12.appeventos.entities.Event
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_events.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URL
import java.util.concurrent.Executors
import com.android.volley.*
import com.example.scb12.appeventos.entities.Category
import kotlinx.android.synthetic.main.event_row.*
import org.jetbrains.anko.support.v4.uiThread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EventsFragment : Fragment(), SearchView.OnQueryTextListener {

    companion object {
        enum class EventsType {
            Events
        }

        private const val TYPE = "type"

        fun newInstance(activityType: EventsFragment.Companion.EventsType,
                        param1: ArrayList<Event>,
                        param2:ArrayList<Event>) =
                EventsFragment().apply {
                    arguments = Bundle().apply {
                        putString(EventsFragment.TYPE, activityType.name)
                        putSerializable("eventList", param1)
                        putSerializable("favList", param2)
                    }
                }
    }

    private var jsonString: String = ""
    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var activity: MainActivity private set
    lateinit var binding: FragmentEventsBinding private set
    private lateinit var eventsType: EventsType
    private val client = OkHttpClient()
    private lateinit var category: Category

    val items: ArrayList<String> = ArrayList()
    val jsonItems: ArrayList<String> = ArrayList()
    var eventList: ArrayList<Event> = ArrayList()
    var favList: ArrayList<Event> = ArrayList()


    private val url = "https://www.eventbriteapi.com/v3/events/search/?search_type=name&token=EGCNBKQRZWJAAPOFDFVJ&" /*"https://api.songkick.com/api/3.0/events/37063834.json?apikey=jWEZBlabQchuiZTC"*/

    val mAdapter: EventsAdapter by lazy{
        EventsAdapter(this, eventList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity = getActivity() as MainActivity
        val bundle: Bundle? = arguments
        if(bundle != null){
            eventList = bundle.getSerializable("eventList") as ArrayList<Event>
            favList = bundle.getSerializable("favList") as ArrayList<Event>

        }
        assignFavs()


    }

    private fun assignFavs(){
        for (event: Event in favList){
            val index = eventList.indexOf(event)
            if(index >= 0){
                eventList[index].isFav = true

            }
        }
    }

   /* private fun loadData() {
        doAsync {
            val requestQueue = Volley.newRequestQueue(activity)
            val objectRequest = JsonObjectRequest(com.android.volley.Request.Method.GET, url,
                null, com.android.volley.Response.Listener<JSONObject> { response ->
                    val jsonArray = response?.getJSONArray("events")
                    for (i in 0..(jsonArray!!.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val event = Event(
                            jsonObject!!.getString("id"),
                            jsonObject.getJSONObject("name").getString("text"),
                            jsonObject.getJSONObject("start").getString("utc"),
                            jsonObject.getJSONObject("end").getString("utc"),
                            jsonObject.getString("url"),
                            jsonObject.getString("logo_id"),
                            jsonObject.getJSONObject("logo").getString("url"),
                            jsonObject.getString("venue_id"),
                            jsonObject.getString("category_id"),
                            jsonObject.getString("is_free"),
                            jsonObject.getJSONObject("description").getString("text"),
                            false
                        )
                        uiThread {
                            eventList.add(event)
                            mAdapter.addItems(eventList)
                            rv.adapter = mAdapter
                        }
                    }
                },
                com.android.volley.Response.ErrorListener { })
            requestQueue.add(objectRequest)

        }*/



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        mAdapter.addItems(eventList)
        binding.rv.adapter = mAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        activity.binding.abl.visibility = View.GONE
        activity.binding.nsv.visibility = View.GONE
        activity.setTitle(R.string.events)
        println("PRIMER LOGGGGGGGGGGGGGGGGGGGGGGGG")
//        loadData()
        println("SEGUNDO LOGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG")
        mAdapter.addItems(eventList)
        rv.adapter = mAdapter
        arguments?.let {
            eventsType = EventsType.valueOf(it.getString(TYPE)!!)
        }

        activity.setSupportActionBar(binding.tb)
        val actionBar = activity.supportActionBar
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        drawerToggle = ActionBarDrawerToggle(activity, activity.binding.dl,
            binding.tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        activity.binding.dl.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        println("TERCER LOGGGGGGGGGGGGGG")
        val mLayoutManager = LinearLayoutManager(context)

        binding.rv.layoutManager = mLayoutManager
        binding.rv.itemAnimator = DefaultItemAnimator()
        binding.rv.adapter = mAdapter
        println("CUARRTO LOGGGGGGGGGGGG")
        //rv.addItemDecoration(DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL))

      //  rellenar()

        binding.srl.setOnRefreshListener {
            binding.srl.post {
                binding.srl.isRefreshing = false
            }
        }

       /* if(mAdapter.rowItemCount == 0) {
            rellenar()
        }*/

       // binding.rv.adapter = mAdapter
    }

   /* private fun rellenar() {
        items.add(jsonString)
        mAdapter.addItems(items)
    }*/

 /*  var i = 0
        while(i < 25) {
            items.add("Evento " + i)
            i++
        }*/
        //mAdapter.addItems(items)


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if(::drawerToggle.isInitialized) {
            drawerToggle.onConfigurationChanged(newConfig)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.binding.nv.menu.findItem(R.id.nav_events).isChecked = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_events, menu)
        val searchItem = menu?.findItem(R.id.search_events)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        if(p0 != null) {
            mAdapter.filter(p0)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if(p0 != null) {
            mAdapter.filter(p0)
        }
        return true
    }

     fun getCategory(id: String) {
        val url = "https://www.eventbriteapi.com/v3/categories/" + id + "/?token=EGCNBKQRZWJAAPOFDFVJ&"

        doAsync {
            val requestQueue = Volley.newRequestQueue(activity)
            val objectRequest = JsonObjectRequest(Request.Method.GET, url,
                null, com.android.volley.Response.Listener<JSONObject> { response ->
                    val name = response?.getString("name")
                    val id = response?.getString("id")
                    category = Category(
                       id.toString(), name.toString()
                    )
                    mAdapter.addCategory(category)

                }, com.android.volley.Response.ErrorListener { })
            requestQueue.add(objectRequest)
        }


     }

    fun addFav(event: Event){
        activity.addFav(event)
    }

    fun removeFav(event: Event){
        activity.removeFav(event)
    }
}
