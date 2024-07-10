package com.example.sallonappbarbar.appUi.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.model.ChatModel
import com.example.sallonappbarbar.data.model.Message
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
): ViewModel() {
 var barberChat = mutableStateOf<List<ChatModel>>(emptyList())
    var _messageList = mutableStateListOf<Message>()
    var messageList: SnapshotStateList<Message> = _messageList

    suspend fun onEvent(event: MessageEvent) {
        when(event){
            is MessageEvent.AddChat -> addChat(event.message,event.barberuid)
            is MessageEvent.GetChatBarber -> getChatBarber()
            is MessageEvent.MessageList -> getmessageList(event.barberuid)
        }
    }

    private suspend fun addChat(message: Message, barberuid:String){
        viewModelScope.launch {
            repo.addChat(message,barberuid)
        }
    }
    private suspend fun getChatBarber(){
        viewModelScope.launch {
          barberChat.value=  repo.getChatBarber()
        }
        Log.d("uses",barberChat.toString())
    }
    private suspend fun getmessageList(barberuid:String){
        viewModelScope.launch {
             repo.messageList(barberuid).collect{
                 message->
                 _messageList.clear()
                 _messageList.addAll(message)
             }
        }
    }
}

sealed class MessageEvent {
    data class AddChat(val message: Message,val barberuid:String):MessageEvent()
    data object GetChatBarber:MessageEvent()
    data class MessageList(val barberuid:String):MessageEvent()
}