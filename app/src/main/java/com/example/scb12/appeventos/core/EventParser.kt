/*
package com.example.scb12.appeventos.core

import com.example.scb12.appeventos.entities.Event
import java.io.InputStream
import java.net.URL
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

class EventParser(url: String) {

    private var rssUrl: URL = URL(url)

    fun parse(): ArrayList<Event> {
        val factory: SAXParserFactory = SAXParserFactory.newInstance()
        val parser: SAXParser = factory.newSAXParser()
        val handler = EventHandler()
        parser.parse(this.getInputStream(), handler)
        return handler.getEvents()
    }

    fun getInputStream(): InputStream {
        return rssUrl.openConnection().getInputStream()
    }
}*/
