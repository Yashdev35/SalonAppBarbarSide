package com.example.sallonappbarbar.appUi.viewModel

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.appUi.utils.showMsg
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.data.model.aService
import com.practicecoding.sallonapp.appui.components.CommonDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarberDataViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel() {

    suspend fun addUserData(barberModel: BarberModel,imageUri: Uri?, activity: Activity) =repo.addUser(barberModel,imageUri)
    suspend fun addServiceData(aServices: List<ServiceType>, activity: Activity) =repo.addServices(aServices)
    suspend fun isShopOpen(shopOpen:Boolean, activity: Activity) =repo.isShopOpen(shopOpen)
    suspend fun getBarberData(activity: Activity) = repo.getBarberData()
//    {
//
////        viewModelScope.launch {
////            repo.addUser(barberModel, imageUri).collect {
////                when (it) {
////                    is Resource.Success -> {
////                        activity.showMsg(it.result)
////                    }
////
////                    is Resource.Failure -> {
////                        activity.showMsg(it.exception.toString())
////                    }
////
////                    Resource.Loading -> {
//////                        CommonDialog()
////                    }
////                }
////            }
////
////
////        }
//    }
}






