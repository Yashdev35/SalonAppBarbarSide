package com.example.sallonappbarbar.appUi.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.model.ChatModel
import com.example.sallonappbarbar.data.model.LastMessage
import com.example.sallonappbarbar.data.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {

    private var _barberChat = MutableStateFlow(emptyList<ChatModel>().toMutableList())
    var barberChat: StateFlow<MutableList<ChatModel>> = _barberChat.asStateFlow()

    private var _messageList = MutableStateFlow(emptyList<Message>().toMutableList())
    var messageList: StateFlow<MutableList<Message>> = _messageList.asStateFlow()

    //    private
    private var _newChat = MutableStateFlow(false)
    var newChat: StateFlow<Boolean> = _newChat.asStateFlow()

    init {
        viewModelScope.launch { getChatBarber() }
    }

    suspend fun onEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.AddChat -> addChat(event.message, event.barberuid, event.status)
            is MessageEvent.GetChatBarber -> getChatBarber()
            is MessageEvent.MessageList -> getmessageList(event.barberuid)
        }
    }

    private suspend fun addChat(message: LastMessage, barberuid: String, status: Boolean) {
        viewModelScope.launch {
            repo.addChat(message, barberuid, status)
        }
    }

    private suspend fun getChatBarber() {
        viewModelScope.launch {
            repo.getChatBarber().collect { chat ->
                _barberChat.emit(chat)
                _newChat.value = false
                for (i in _barberChat.value) {
                    if (!i.message.seenbybarber) {
                        _newChat.emit(true)
                        break
                    }
                }
            }
        }
        Log.d("uses", barberChat.toString())
    }

    private suspend fun getmessageList(barberuid: String) {
        viewModelScope.launch {
            repo.messageList(barberuid).collect { message ->
                _messageList.update { it.apply { clear() } }
                _messageList.emit(message)
            }
        }
    }
}

sealed class MessageEvent {
    data class AddChat(
        val message: LastMessage,
        val barberuid: String,
        val status: Boolean = true
    ) : MessageEvent()

    data object GetChatBarber : MessageEvent()
    data class MessageList(val barberuid: String) : MessageEvent()
}