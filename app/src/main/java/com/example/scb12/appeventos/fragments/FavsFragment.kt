package com.example.scb12.appeventos.fragments


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.*
import android.view.*
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.scb12.appeventos.MainActivity

import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.adapters.FavsAdapter
import com.example.scb12.appeventos.databinding.FragmentFavsBinding
import com.example.scb12.appeventos.entities.Category
import com.example.scb12.appeventos.entities.Event
import kotlinx.android.synthetic.main.fragment_favs.*
import org.jetbrains.anko.doAsync
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FavsFragment : Fragment(), SearchView.OnQueryTextListener {

    companion object {
        enum class FavsType {
            Favs
        }
        private const val TYPE = "type"

        fun newInstance(activityType: FavsFragment.Companion.FavsType, param1: ArrayList<Event>) =
            FavsFragment().apply {
                arguments = Bundle().apply {
                    putString(FavsFragment.TYPE, activityType.name)
                    putSerializable("list", param1)
                }
            }
//    @JvmStatic fun newInstance() = FavsFragment()
    }

    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var activity: MainActivity private set
    lateinit var binding: FragmentFavsBinding private set
    private lateinit var favsType: FavsType
    private lateinit var category: Category

    private var favsList: ArrayList<Event> = ArrayList()

//    private val favListViewModel by lazy{
//        ViewModelProviders.of(activity).get(FavsListViewModel::class.java)
//    }
//    private lateinit var favListViewModel: FavsListViewModel

    val mAdapter: FavsAdapter by lazy{
        FavsAdapter(this, favsList)
    }

//    override fun onStart() {
//        super.onStart()
//        loadData(favListViewModel)
//
//    }

    fun addFav(event: Event){
        favsList.add(event)
    }

    private fun loadData(){
        mAdapter.addItems(favsList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val bundle: Bundle? = arguments
        if(bundle != null) {
            favsList = bundle.getSerializable("list") as ArrayList<Event>
        }
//        activity = getActivity() as MainActivity
//        favListViewModel = activity?.run{
//            ViewModelProviders.of(this).get(FavsListViewModel::class.java)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentFavsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        activity.binding.abl.visibility = View.GONE
        activity.binding.nsv.visibility = View.GONE
        activity.setTitle(R.string.favs)


        rv.adapter = mAdapter

        arguments?.let {
            favsType = FavsType.valueOf(it.getString(TYPE)!!)
        }

        activity.setSupportActionBar(binding.tb)
        val actionBar = activity.supportActionBar
        if(actionBar != null){
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

        binding.srl.setOnRefreshListener{
            binding.srl.post{
                binding.srl.isRefreshing = false
            }
        }
        loadData()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if(::drawerToggle.isInitialized) {
            drawerToggle.onConfigurationChanged(newConfig)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity.binding.nv.menu.findItem(R.id.nav_favs).isChecked = false
    }

//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        inflater?.inflate(R.menu.menu_events, menu)
//        val searchItem = menu?.findItem(R.id.search_events)
//        val searchView = searchItem?.actionView as SearchView
//        searchView.setOnQueryTextListener(this)
//        super.onCreateOptionsMenu(menu, inflater)
//    }

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

    fun getCategory(id: String) {
        val url = "https://www.eventbriteapi.com/v3/categories/$id/?token=EGCNBKQRZWJAAPOFDFVJ&"

        doAsync {
            val requestQueue = Volley.newRequestQueue(activity)
            val objectRequest = JsonObjectRequest(Request.Method.GET, url,
                null, Response.Listener<JSONObject> { response ->
                    val name = response?.getString("name")
                    val id = response?.getString("id")
                    category = Category(
                        id.toString(), name.toString()
                    )
                    mAdapter.addCategory(category)

                }, Response.ErrorListener {})
            requestQueue.add(objectRequest)
        }



    }

//    fun addFav(event: Event){
//        val id = event.id
//        var index = 0
//        for (ev: Event in eventList){
//            if(id == ev.id)
//                index = eventList.indexOf(ev)
//            eventList[index] = event
//        }
//        activity.addFav(event)
//    }

    fun removeFav(event: Event){
        favsList.remove(event)
        activity.removeFav(event)
    }



}
