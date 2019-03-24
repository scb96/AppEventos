package com.example.scb12.appeventos.fragments


import android.content.res.Configuration
import android.os.Bundle
import android.provider.Contacts
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.*
import android.view.*
import com.example.scb12.appeventos.MainActivity

import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.adapters.EventsAdapter
import com.example.scb12.appeventos.databinding.FragmentEventsBinding
import com.example.scb12.appeventos.entities.Event
import com.example.scb12.appeventos.entities.GetData
import com.example.scb12.appeventos.entities.Request
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_events.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.util.concurrent.Executors

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

    val items: ArrayList<String> = ArrayList()
    val jsonItems: ArrayList<String> = ArrayList()

    val mAdapter: EventsAdapter by lazy{
        EventsAdapter(this, jsonItems)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val url = "https://www.eventbriteapi.com/v3/events/search/?search_type=name&token=EGCNBKQRZWJAAPOFDFVJ&"

        Executors.newSingleThreadExecutor().execute({
            val json = URL(url).readText()
            jsonItems.addAll(json.split('{'))
        })

        jsonItems
        mAdapter.addItems(jsonItems)
        //llenarJson(url)


    }

    private fun llenarJson(url: String){

/*
                val jsonString = URL(url).readText()
                var array: ArrayList<String> = ArrayList()
                array.addAll(jsonString.split('{'))
                mAdapter.addItems(array)*/

    }
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

        rv.addItemDecoration(DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL))

      //  rellenar()

        binding.srl.setOnRefreshListener {
            binding.srl.post {
                binding.srl.isRefreshing = false
            }
        }

       /* if(mAdapter.rowItemCount == 0) {
            rellenar()
        }*/

        binding.rv.adapter = mAdapter
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


}
