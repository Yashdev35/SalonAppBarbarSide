package com.example.sallonappbarbar.data


import android.net.Uri
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ServiceCat
import com.example.sallonappbarbar.data.model.Slots
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend  fun addUser(
        barberModel: BarberModel,
        imageUri: Uri?
//        name:String,phoneNumber:String,dateOfBirth:String,gender:String,imageUri:String
    ) : Flow<Resource<String>>
    suspend fun addServices(
        service: List<ServiceCat>,
    ) : Flow<Resource<String>>
    suspend fun setSlots(
        openCloseTime:List<Slots>
    ): Flow<Resource<String>>

    suspend fun getBarberData(): Flow<Resource<BarberModel>>
    suspend fun getTimeSlot(): List<Slots>

    suspend fun getBarber(uid:String?):BarberModel?
    suspend fun updateBookedSlots(times:List<String>, day:String):Flow<Resource<String>>
    suspend fun updateNotAvailableSlots(times:List<String>, day:String):Flow<Resource<String>>
}