package com.example.sallonappbarbar.data


import android.net.Uri
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.data.model.WeekDay
import com.example.sallonappbarbar.data.model.aService
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend  fun addUser(
        barberModel: BarberModel,
        imageUri: Uri?
//        name:String,phoneNumber:String,dateOfBirth:String,gender:String,imageUri:String
    ) : Flow<Resource<String>>
    suspend fun addServices(
        aServices: List<ServiceType>,
    ) : Flow<Resource<String>>
    suspend fun isShopOpen(
        shopOpen:Boolean
    ) : Flow<Resource<String>>

    suspend fun addOpenCloseTime(
        openCloseTime:String
    ) : Flow<Resource<String>>

    suspend fun addSlots(
        weekDays: List<WeekDay>
    ): Flow<Resource<String>>

    suspend fun getOpenCloseTime(): Flow<Resource<String>>
    suspend fun getBarberData(): Flow<Resource<BarberModel>>

}