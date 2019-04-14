package com.example.scb12.appeventos.fragments


import android.content.ContentValues
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v14.preference.R.layout.preference
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.scb12.appeventos.MainActivity

import com.example.scb12.appeventos.R
import com.example.scb12.appeventos.Utils.Utils
import com.example.scb12.appeventos.database.SQLiteHelperConection
import com.example.scb12.appeventos.databinding.FragmentSettingsBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.pass_dialog.*
import kotlinx.android.synthetic.main.pass_dialog.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SettingsFragment : Fragment() {

    companion object {
        enum class SettingsType {
            Settings
        }

        private const val TYPE = "type"

        fun newInstance(activityType: SettingsFragment.Companion.SettingsType) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(SettingsFragment.TYPE, activityType.name)
                }
            }
    }

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var activity: MainActivity
    private lateinit var settingsType: SettingsType
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        activity = getActivity() as MainActivity

        binding.bPass.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.pass_dialog, null)
            val builder = AlertDialog.Builder(activity)
                .setTitle(R.string.pass_change)
                .setView(dialogView)
                .setPositiveButton("OK") {_, _ ->
                    val oldPassword = dialogView.etOldPass.text.toString()
                    val newPassword = dialogView.etNewPass.text.toString()
                    val confirmPass = dialogView.etConfirmPass.text.toString()

                    if(newPassword != confirmPass) {
                        Toast.makeText(activity,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                        dialogView.etOldPass.text.clear()
                        dialogView.etNewPass.text.clear()
                        dialogView.etConfirmPass.text.clear()
                    }
                    val conn = SQLiteHelperConection(activity, "bd_appEventos", null, 1)
                    val db = conn.writableDatabase

                    val values = ContentValues()
                    values.put(Utils().PASSWORD_FIELD, newPassword)

                    val res = db.update(Utils().TABLE_USERS, values, Utils().PASSWORD_FIELD, null)
                    Toast.makeText(activity, "PASSWORD CHANGED", Toast.LENGTH_LONG).show()
                    db.close()

                }

            val dialog = builder.create()
            dialog.show()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        activity.setTitle(R.string.settings)
        activity.binding.abl.visibility = View.GONE
        activity.binding.nsv.visibility = View.GONE





        arguments?.let {
            settingsType = SettingsType.valueOf(it.getString(TYPE)!!)
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
    }




}
