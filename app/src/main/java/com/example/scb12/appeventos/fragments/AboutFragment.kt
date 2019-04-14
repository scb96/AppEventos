package com.example.scb12.appeventos.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scb12.appeventos.MainActivity

import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.databinding.FragmentAboutBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AboutFragment : Fragment() {



    companion object {
        enum class AboutType {
            About
        }


        private const val TYPE = "type"

        fun newInstance(activityType: AboutFragment.Companion.AboutType) =
            AboutFragment().apply {
                arguments = Bundle().apply {
                    putString(AboutFragment.TYPE, activityType.name)
                }
            }
    }

    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var activity: MainActivity private set
    lateinit var binding: FragmentAboutBinding private set


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
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        activity.binding.abl.visibility = View.GONE
        activity.binding.nsv.visibility = View.GONE
        activity.setTitle(R.string.about)

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
    }

}
