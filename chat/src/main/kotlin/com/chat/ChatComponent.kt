package com.chat

import javax.inject.Singleton
import com.chat.ChatApp
import dagger.Component

@Singleton
@Component
interface ChatComponent {
    fun build(): ChatApp
}