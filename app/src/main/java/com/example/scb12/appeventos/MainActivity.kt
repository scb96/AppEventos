package com.example.scb12.appeventos

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import com.example.scb12.appeventos.core.NavHeaderViewHolder
import com.example.scb12.appeventos.databinding.ActivityMainBinding

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

    private var currentDrawerItemID: Int = 0
    private var drawerClickStatus: Boolean = false

   /* private val eventsFragment: EventsFragment by lazy {
        EventsFragment.newInstance(EventsFragment.Companion.EventsType.Events)
    }*/

    /* private val eventsFragment: FavsFragment by lazy {
        FavsFragment.newInstance(FavsFragment.Companion.FavsType.Favs)
    }*/

    /* private val eventsFragment: MyEventsFragment by lazy {
        MyEventsFragment.newInstance(MyEventsFragment.Companion.MyEventsType.MyEvents)
    }*/

    /* private val eventsFragment: AboutFragment by lazy {
        AboutFragment.newInstance(AboutFragment.Companion.AboutType.About)
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

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
        setTitle(R.string.app_name)

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

        binding.nv.setNavigationItemSelectedListener { item ->
           when(item.itemId) {
              /* R.id.nav_events -> {
                   if (currentDrawerItemID != ACTION_EVENTS) {
                       loadFragment(ACTION_EVENTS)
                   } true
               }*/

               /* R.id.nav_favs -> {
                   if (currentDrawerItemID != ACTION_FAVS) {
                       loadFragment(ACTION_FAVS)
                   } true
               }*/

              /* R.id.nav_my_events -> {
                   if (currentDrawerItemID != ACTION_MY_EVENTS) {
                       loadFragment(ACTION_MY_EVENTS)
                   } true
               }*/

              /* R.id.nav_favs -> {
                   if (currentDrawerItemID != ACTION_ABOUT) {
                       loadFragment(ACTION_ABOUT)
                   } true
               }*/
           }
        }

        if(savedInstanceState == null) {
            loadFragment(ACTION_EVENTS)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    private fun loadFragment(currenDrawerItemID: Int) {
        clearBackStack()
        this.currentDrawerItemID = currentDrawerItemID
        when(currenDrawerItemID) {
           /* ACTION_EVENTS -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, eventsFragment, R.string.events)
                    .commit()
            }*/

            /*ACTION_FAVS -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, favsFragment, R.string.favs)
                    .commit()
            }*/

           /* ACTION_MY_EVENTS -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, myEventsFragment, R.string.my_events)
                    .commit()
            }*/

           /* ACTION_ABOUT -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.clMain, aboutFragment, R.string.about)
                    .commit()
            }*/
        }
    }

    private fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStackImmediate()
        }
    }
}
