package com.example.scb12.appeventos

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.scb12.appeventos.activities.LoginActivity
import com.example.scb12.appeventos.core.NavHeaderViewHolder
import com.example.scb12.appeventos.databinding.ActivityMainBinding
import com.example.scb12.appeventos.entities.Event
import com.example.scb12.appeventos.fragments.AboutFragment
import com.example.scb12.appeventos.fragments.EventsFragment
import com.example.scb12.appeventos.fragments.FavsFragment
import com.example.scb12.appeventos.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_nav_header.*
import kotlinx.android.synthetic.main.fragment_events.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        private const val ACTION_EVENTS = 1
        private const val ACTION_FAVS = 2
        private const val ACTION_MY_EVENTS = 3
        private const val ACTION_SETTINGS = 4
        private const val ACTION_ABOUT = 5
    }

    lateinit var binding: ActivityMainBinding private set
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navHeader: NavHeaderViewHolder
    private var saved: Bundle? = null

    private var currentDrawerItemID: Int = 0
    private var drawerClickStatus: Boolean = false
    private val favsList: ArrayList<Event> = ArrayList()
    private val eventList: ArrayList<Event> = ArrayList()

    private val eventsFragment: EventsFragment by lazy {
        EventsFragment.newInstance(EventsFragment.Companion.EventsType.Events,
            eventList, favsList)
    }

    private val favsFragment: FavsFragment by lazy {
        FavsFragment.newInstance(FavsFragment.Companion.FavsType.Favs, favsList)
    }

    /* private val eventsFragment: MyEventsFragment by lazy {
        MyEventsFragment.newInstance(MyEventsFragment.Companion.MyEventsType.MyEvents)
    }*/

    private val settingsFragment: SettingsFragment by lazy {
        SettingsFragment.newInstance(SettingsFragment.Companion.SettingsType.Settings)
    }

     private val aboutFragment: AboutFragment by lazy {
        AboutFragment.newInstance(AboutFragment.Companion.AboutType.About)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val prefs = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("LOGIN", true)
        editor.apply()
        loadData()
    }

    @SuppressLint("SetTextI18n")
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setSupportActionBar(binding.tb)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.tb.setNavigationOnClickListener {
            binding.dl.openDrawer(GravityCompat.START)
        }
        setTitle(R.string.events)

        drawerToggle = ActionBarDrawerToggle(
            this, binding.dl, binding.tb,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.dl.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val view = binding.nv.getHeaderView(0)
        if(view != null) {
            navHeader = NavHeaderViewHolder(view)
        }

        drawerClickStatus = false

        navHeader.menuToggle.setOnClickListener {
            val menu = binding.nv.menu
            if(drawerClickStatus) {
                menu.setGroupVisible(R.id.nav_menu_1, true)
                menu.setGroupVisible(R.id.nav_menu_2, true)
            }
            drawerClickStatus = !drawerClickStatus
        }
        binding.nv.setNavigationItemSelectedListener { item ->
           when(item.itemId) {
               R.id.nav_events -> {
                   if (currentDrawerItemID != ACTION_EVENTS) {
                       loadFragment(ACTION_EVENTS)
                       binding.dl.closeDrawer(nv)
                   }
                   true
               }

               R.id.nav_favs -> {
                   if (currentDrawerItemID != ACTION_FAVS) {
                       loadFragment(ACTION_FAVS)
                       binding.dl.closeDrawer(nv)
                   }
                   true
               }

              /* R.id.nav_my_events -> {
                   if (currentDrawerItemID != ACTION_MY_EVENTS) {
                       loadFragment(ACTION_MY_EVENTS)
                       binding.dl.closeDrawer(nv)
                   } true
               }*/

                R.id.nav_settings -> {
                   if (currentDrawerItemID != ACTION_SETTINGS) {
                       loadFragment(ACTION_SETTINGS)
                       binding.dl.closeDrawer(nv)
                   }
                    true
               }

               R.id.nav_about -> {
                   if (currentDrawerItemID != ACTION_ABOUT) {
                       loadFragment(ACTION_ABOUT)
                       binding.dl.closeDrawer(nv)
                   }
                   true
               }
           else -> { true }
           }
        }

        val name = intent.extras?.getString("NAME")
        val lastName = intent.extras?.getString("LASTNAME")
        val username = intent.extras?.getString("USERNAME")

        navHeader.name.setText("$name $lastName")
        navHeader.username.text = username

        navHeader.closeSession.setOnClickListener {
            val prefs = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean("LOGIN", false)
            editor.apply()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        saved = savedInstanceState
      /*  if(savedInstanceState == null) {
            loadData()
            loadFragment(ACTION_EVENTS)
        }*/
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    private fun loadFragment(currenDrawerItemID: Int) {
        clearBackStack()
        this.currentDrawerItemID = currentDrawerItemID
        when(currenDrawerItemID) {
            ACTION_EVENTS -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, eventsFragment, getString(R.string.events))
                    .commit()
            }

            ACTION_FAVS -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, favsFragment, getString(R.string.favs))
                    .commit()
            }

           /* ACTION_MY_EVENTS -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, myEventsFragment, R.string.my_events)
                    .commit()
            }*/

            ACTION_SETTINGS -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, settingsFragment, getString(R.string.settings))
                    .commit()
            }

            ACTION_ABOUT -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, aboutFragment, getString(R.string.about))
                    .commit()
            }
        }
    }

    private fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStackImmediate()
        }
    }

    fun addFav(event: Event){
        favsList.add(event)
    }

    fun removeFav(event: Event){
        favsList.remove(event)
    }

    private fun loadData() {
        val url = "https://www.eventbriteapi.com/v3/events/search/?search_type=name&token=EGCNBKQRZWJAAPOFDFVJ&" /*"https://api.songkick.com/api/3.0/events/37063834.json?apikey=jWEZBlabQchuiZTC"*/

        doAsync {
            val requestQueue = Volley.newRequestQueue(this@MainActivity)
            val objectRequest = JsonObjectRequest(
                Request.Method.GET, url,
                null, Response.Listener<JSONObject> { response ->
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
                            if(saved == null) {
                                pb.visibility = View.GONE
                                loadFragment(ACTION_EVENTS)
                            }
//                            mAdapter.addItems(eventList)
//                            rv.adapter = mAdapter
                        }
                    }
                },
                Response.ErrorListener {})
            requestQueue.add(objectRequest)

        }
    }

}

