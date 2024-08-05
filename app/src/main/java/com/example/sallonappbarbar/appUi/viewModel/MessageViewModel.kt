package com.example.sallonappbarbar.appUi.viewModel

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.model.LastChatModel
import com.example.sallonappbarbar.data.model.LastMessage
import com.example.sallonappbarbar.data.model.Message
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
): ViewModel() {
 var barberChat = mutableStateOf<List<LastChatModel>>(emptyList())
    var _messageList = mutableStateListOf<Message>()
    var messageList: SnapshotStateList<Message> = _messageList
    var _count = mutableIntStateOf(0)
    init {
        viewModelScope.launch { getChatBarber() }
    }
    suspend fun onEvent(event: MessageEvent) {
        when(event){
            is MessageEvent.AddChat -> addChat(event.message,event.barberuid,event.status)
            is MessageEvent.GetChatBarber -> getChatBarber()
            is MessageEvent.MessageList -> getmessageList(event.barberuid)
        }
    }

    private suspend fun addChat(message: LastMessage, barberuid:String,status:Boolean){
        viewModelScope.launch {
            repo.addChat(message,barberuid,status)
        }
    }
    private suspend fun getChatBarber(){
        viewModelScope.launch {
            repo.getChatBarber().collect { chat ->
             barberChat.value = chat
                _count.value=0
                for (i in barberChat.value) {
                    if (!i.message.seenbybarber) {
                        _count.value++
                    }
                    if (_count.value > 1) {
                        break
                    }
                }
          }
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
    data class AddChat(val message: LastMessage,val barberuid:String,val status:Boolean=true):MessageEvent()
    data object GetChatBarber:MessageEvent()
    data class MessageList(val barberuid:String):MessageEvent()
}