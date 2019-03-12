package com.example.scb12.appeventos.core

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.TextView
import com.example.scb12.appeventos.R
import kotlinx.android.synthetic.main.activity_main_nav_header.view.*

class NavHeaderViewHolder(view: View) {
    val name: TextView = view.findViewById(R.id.header_name)
    val details: TextView = view.findViewById(R.id.header_details)
    val menuToggle: ConstraintLayout = view.findViewById(R.id.menuToggle)

}