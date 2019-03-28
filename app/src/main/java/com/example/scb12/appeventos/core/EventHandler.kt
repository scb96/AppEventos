/*
package com.example.scb12.appeventos.core

import com.example.scb12.appeventos.entities.Event
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.lang.StringBuilder

class EventHandler : DefaultHandler() {

    private lateinit var events : ArrayList<Event>
    private lateinit var actualEvent : Event
    private lateinit var sbText: StringBuilder
    var error: Boolean = false

    fun getEvents() : ArrayList<Event> {
        return events
    }

    override fun startDocument() {
        super.startDocument()
        events = ArrayList()
        sbText = StringBuilder()
    }

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes?) {
        super.startElement(uri, localName, qName, attributes)
        if(localName.equals("event")) {
            actualEvent = Event("0", qName, "0")
        }
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        super.characters(ch, start, length)

        if(this.actualEvent != null) {
            sbText.append(ch, start, length)
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)
        if(this.actualEvent != null) {
            if(localName.equals("event")) {
              val array: ArrayList<String> = ArrayList()
                val char = '"'
              array.addAll(localName!!.split(char + " "))
                for(i in array) {
                    if(i.startsWith("id")) {
                        actualEvent.id = i.substring(4, i.length - 1)
                    }
                }
            } else if(localName.equals("/event")) {
                events.add(actualEvent)
            }
            sbText.setLength(0)
        }
    }

}*/
