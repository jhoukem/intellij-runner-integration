package com.chat

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val chatApp = DaggerChatComponent.create().build()
        chatApp.displayAppInfo()
    }
}