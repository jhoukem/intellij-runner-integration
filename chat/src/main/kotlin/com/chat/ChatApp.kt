package com.chat

import javax.inject.Inject
import com.core.AppProperties
import java.io.IOException

class ChatApp @Inject constructor() {
    fun displayAppInfo() {
        try {
            println("The app version is " + AppProperties.getVersion())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}