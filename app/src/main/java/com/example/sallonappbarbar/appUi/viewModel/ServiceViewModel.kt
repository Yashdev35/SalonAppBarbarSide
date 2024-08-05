package com.example.sallonappbarbar.appUi.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.ServiceCat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {
    private val _serviceList = mutableStateOf<List<ServiceCat>>(emptyList())
    val serviceList : State<List<ServiceCat>> = _serviceList

    init {
        viewModelScope.launch{
            repo.getServices().collect {
                when(it){
                    is Resource.Success -> {
                        _serviceList.value = it.result.toMutableList()
                    }
                    is Resource.Failure -> {
                        Log.d("ServiceViewModel", "init: ${it.exception}")
                    }
                    is Resource.Loading -> {
                        Log.d("ServiceViewModel", "init: Loading")
                    }
                }
            }
        }
    }
    suspend fun getServiceList(){
        repo.getServices().collect {
            when(it){
                is Resource.Success -> {
                    _serviceList.value = it.result.toMutableList()
                }
                is Resource.Failure -> {
                    Log.d("ServiceViewModel", "init: ${it.exception}")
                }
                is Resource.Loading -> {
                    Log.d("ServiceViewModel", "init: Loading")
                }
            }
        }
    }

}