package com.example.sallonappbarbar.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Message(val status: Boolean, val message: String, val time: String):Parcelable

@Serializable
@Parcelize
data class LastMessage(val status: Boolean, val message: String, val time: String,
                       var seenbybarber:Boolean, val seenbyuser:Boolean):Parcelable

@Serializable
@Parcelize
data class ChatModel(val name: String,val message:LastMessage,val image:String,val uid:String,val phoneNumber:String):Parcelable
