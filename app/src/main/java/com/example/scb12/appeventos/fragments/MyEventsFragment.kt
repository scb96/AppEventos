package com.example.scb12.appeventos.fragments


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.example.scb12.appeventos.MainActivity

import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.adapters.MyEventsAdapter
import com.example.scb12.appeventos.databinding.FragmentMyEventsBinding
import com.example.scb12.appeventos.entities.Event
import kotlinx.android.synthetic.main.dialog_new_event.*
import kotlinx.android.synthetic.main.dialog_new_event.view.*
import kotlinx.android.synthetic.main.fragment_my_events.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyEventsFragment : Fragment(), SearchView.OnQueryTextListener {

    companion object {
        enum class MyEventsType {
            MyEvents
        }

        private const val TYPE = "type"

        fun newInstance(activityType: MyEventsFragment.Companion.MyEventsType) =
            MyEventsFragment().apply {
                arguments = Bundle().apply {
                    putString(MyEventsFragment.TYPE, activityType.name)
                }
            }
    }

    private var name: String = ""
    private var startDate: String = ""
    private var finishDate: String = ""
    private var location: String = ""
    private var isFree: String = "false"
    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var activity: MainActivity private set
    lateinit var binding: FragmentMyEventsBinding private set
    private lateinit var myEventsType: MyEventsType
    var myEventsList: ArrayList<Event> = ArrayList()


    val mAdapter: MyEventsAdapter by lazy{
        MyEventsAdapter(this, myEventsList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        activity = getActivity() as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentMyEventsBinding.inflate(inflater, container, false)
        mAdapter.addItems(myEventsList)
        binding.rv.adapter = mAdapter

        binding.fabAdd.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_new_event, null)
            val builder = AlertDialog.Builder(activity)
                .setView(dialogView)
                .setPositiveButton("OK") { dialog, _ ->
                    name = dialogView.etName.text.toString()
                    startDate = dialogView.etDateIni.text.toString()
                    finishDate = dialogView.etDateFin.text.toString()
                    location = dialogView.etLocation.text.toString()
                    isFree = if(dialogView.cbFree.isChecked) {
                        "true"
                    } else "false"
                    val event = Event("", name, startDate, finishDate, "", "", "", "", "", isFree, "", false)
                    myEventsList.add(event)
                    mAdapter.addItems(myEventsList)

                }
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.cancel()
                }

            val dialog = builder.create()
            dialog.show()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        activity.binding.abl.visibility = View.GONE
        activity.binding.nsv.visibility = View.GONE
        activity.setTitle(R.string.my_events)

        arguments?.let {
            myEventsType = MyEventsType.valueOf(it.getString(TYPE)!!)
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
        binding.rv.adapter = mAdapter

        binding.srl.setOnRefreshListener {
            binding.srl.post {
                binding.srl.isRefreshing = false
            }
        }

    }

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
        inflater?.inflate(R.menu.menu_favs, menu)
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
