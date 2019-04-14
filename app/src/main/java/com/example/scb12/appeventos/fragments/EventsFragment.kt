package com.example.scb12.appeventos.fragments


import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Contacts
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
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

        fun newInstance(activityType: EventsFragment.Companion.EventsType) =
                EventsFragment().apply {
                    arguments = Bundle().apply {
                        putString(EventsFragment.TYPE, activityType.name)
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
    val eventList: ArrayList<Event> = ArrayList()


    private val url = "https://www.eventbriteapi.com/v3/events/search/?search_type=name&token=EGCNBKQRZWJAAPOFDFVJ&" /*"https://api.songkick.com/api/3.0/events/37063834.json?apikey=jWEZBlabQchuiZTC"*/

     val mAdapter: EventsAdapter by lazy{
        EventsAdapter(this, eventList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity = getActivity() as MainActivity

    }

    private fun loadData() {
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

                        eventList.add(event)
                        uiThread {
                            mAdapter.addItems(eventList)
                            binding.rv.adapter = mAdapter
                        }
                    }
                },
                com.android.volley.Response.ErrorListener {})
            requestQueue.add(objectRequest)

        }
    }

       /* val queue: RequestQueue = Volley.newRequestQueue(activity)
//        val stringRequest = StringRequest(com.android.volley.Request.Method.GET, url)
         val stringRequest = StringRequest(url, object : com.android.volley.Response.Listener<String> {
                override fun onResponse(response: String?) {
                    val jsonObject = JSONObject(response)
                    val jsonArray: JSONArray = jsonObject.getJSONArray("events")
                    for(i in 0..(jsonArray.length() - 1)) {
                        val jo: JSONObject = jsonArray.getJSONObject(i)
                        val event = Event(jo.getString("id"), jo.getString("displayName"), "")
                        eventList.add(event)
                    }
                }
            }, object : com.android.volley.Response.ErrorListener {
                 override fun onErrorResponse(error: VolleyError?) {

                 }
             })
    }*/

    /*fun run(url: String) {
        val request = okhttp3.Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) { }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                println(response.body()?.string()?.split(","))
            }
        })
    }*/



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        activity.binding.abl.visibility = View.GONE
        activity.binding.nsv.visibility = View.GONE
        activity.setTitle(R.string.events)



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

        val mLayoutManager = LinearLayoutManager(context)

        binding.rv.layoutManager = mLayoutManager
        binding.rv.itemAnimator = DefaultItemAnimator()

        //rv.addItemDecoration(DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL))

      //  rellenar()

        binding.srl.setOnRefreshListener {
            binding.srl.post {
                binding.srl.isRefreshing = false
            }
        }
        binding.rv.adapter = mAdapter
        loadData()


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

/*
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var id = ""
        when(item!!.itemId) {
            R.id.filter_community -> {
                id = "113"
                loadEvents(id)
            }

            R.id.filter_arts -> {
                id = "105"
                loadEvents(id)
            }

            R.id.filter_science -> {
                id = "102"
                loadEvents(id)
            }

            R.id.filter_film -> {
                id = "104"
                loadEvents(id)
            }

            R.id.filter_music -> {
                id = "103"
                loadEvents(id)
            }

            R.id.filter_food -> {
                id = "110"
                loadEvents(id)
            }

            R.id.filter_hobbies -> {
                id = "119"
                loadEvents(id)
            }

            R.id.filter_sports -> {
                id = "108"
                loadEvents(id)
            }

            R.id.filter_health -> {
                id = "107"
                loadEvents(id)
            }

            R.id.filter_home -> {
                id = "117"
                loadEvents(id)
            }

            R.id.filter_fashion -> {
                id = "106"
                loadEvents(id)
            }

            R.id.filter_holiday -> {
                id = "116"
                loadEvents(id)
            }

            R.id.filter_travel -> {
                id = "109"
                loadEvents(id)
            }

            R.id.filter_auto -> {
                id = "118"
                loadEvents(id)
            }

            R.id.filter_family-> {
                id = "115"
                loadEvents(id)
            }

            R.id.filter_school -> {
                id = "120"
                loadEvents(id)
            }

            R.id.filter_charity -> {
                id = "111"
                loadEvents(id)
            }

            R.id.filter_spirituality -> {
                id = "114"
                loadEvents(id)
            }

            R.id.filter_business -> {
                id = "101"
                loadEvents(id)
            }

            R.id.filter_government -> {
                id = "112"
                loadEvents(id)
            }

            R.id.filter_other -> {
                id = "199"
                loadEvents(id)
            }
        }
        return super.onOptionsItemSelected(item)
    }
*/

   /* private fun loadEvents(id: String) {
        mAdapter.clear()
        val rv = activity.findViewById<RecyclerView>(R.id.rv)
        val events: ArrayList<Event> = ArrayList()
        for(i in eventList) {
            if(i.categoryId == id) {
                events.add(i)
            }
        }
      //  eventList.clear()
        mAdapter.addItems(events)
        mAdapter.notifyDataSetChanged()
    }*/

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
        val url = "https://www.eventbriteapi.com/v3/categories/?token=EGCNBKQRZWJAAPOFDFVJ&"

        doAsync {
            val categories: ArrayList<Category> = ArrayList()
            val requestQueue = Volley.newRequestQueue(activity)
            val objectRequest = JsonObjectRequest(Request.Method.GET,
                url,
                null,
                com.android.volley.Response.Listener<JSONObject> { response ->
                    val jsonArray = response?.getJSONArray("categories")
                    for (i in 0..(jsonArray!!.length() - 1)) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val category = Category(
                            jsonObject!!.getString("id"),
                            jsonObject.getString("name")
                        )
                        categories.add(category)
                        for(i in categories) {
                            if(i.id == id) {
                                mAdapter.addCategory(i)
                            }
                        }
                    }
                },
                com.android.volley.Response.ErrorListener {
                    Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show()
                })
            requestQueue.add(objectRequest)
        }

     }
}
