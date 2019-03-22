package com.example.scb12.appeventos.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.EventLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scb12.appeventos.MainActivity

import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.adapters.EventsAdapter
import com.example.scb12.appeventos.databinding.FragmentEventsBinding
import kotlinx.android.synthetic.main.fragment_events.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class EventsFragment : Fragment() {

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

    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var activity: MainActivity private set
    lateinit var binding: FragmentEventsBinding private set
    private lateinit var eventsType: EventsType

    val mAdapter: EventsAdapter by lazy{
        EventsAdapter(this, arrayListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        rellenar()

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
            eventsType = EventsType.valueOf(it.getString(TYPE))
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

        binding.rv.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
        binding.rv.itemAnimator = DefaultItemAnimator()

        rv.addItemDecoration(DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL))



        binding.rv.adapter = mAdapter
    }

    private fun rellenar() {
        val items: ArrayList<String> = ArrayList()
        items.add("EVENTO 1")
        items.add("EVENTO 2")
        items.add("EVENTO 3")
        items.add("EVENTO 4")
        items.add("EVENTO 5")
        items.add("EVENTO 6")

        mAdapter.addRowItems(items)
        mAdapter.notifyDataSetChanged()
    }

}
