package com.example.sallonappbarbar.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(val status: Boolean, val message: String, val time: String)

@Serializable
data class LastMessage(val status: Boolean, val message: String, val time: String,
                       var seenbybarber:Boolean, val seenbyuser:Boolean)

data class LastChatModel(val name: String,val message:LastMessage,val image:String,val uid:String,val phoneNumber:String)
