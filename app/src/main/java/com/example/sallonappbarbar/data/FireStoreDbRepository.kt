package com.example.sallonappbarbar.data


import android.net.Uri
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ChatModel
import com.example.sallonappbarbar.data.model.LastMessage
import com.example.sallonappbarbar.data.model.Message
import com.example.sallonappbarbar.data.model.OrderModel
import com.example.sallonappbarbar.data.model.ReviewModel
import com.example.sallonappbarbar.data.model.ServiceCat
import com.example.sallonappbarbar.data.model.Slots
import kotlinx.coroutines.flow.Flow

interface FireStoreDbRepository {
    suspend fun addBarber(
        barberModel: BarberModel,
        imageUri: Uri?
//        name:String,phoneNumber:String,dateOfBirth:String,gender:String,imageUri:String
    ): Flow<Resource<String>>

    suspend fun updateBarberInfo(
        barberModel: BarberModel,
        imageUri: Uri?
    ): Flow<Resource<String>>

    suspend fun addServices(
        service: List<ServiceCat>,
    ): Flow<Resource<String>>

    suspend fun getServices(): Flow<Resource<List<ServiceCat>>>
    suspend fun setSlots(
        openCloseTime: List<Slots>
    ): Flow<Resource<String>>

    suspend fun getBarberData(): Flow<Resource<BarberModel>>
    suspend fun getTimeSlot(): List<Slots>

    suspend fun getBarber(uid: String?): BarberModel?
    suspend fun updateBookedSlots(times: List<String>, day: String): Flow<Resource<String>>
    suspend fun updateNotAvailableSlots(times: List<String>, day: String): Flow<Resource<String>>

    suspend fun addChat(message: LastMessage, barberUid: String, status: Boolean)
    suspend fun getChatBarber(): Flow<MutableList<ChatModel>>
    suspend fun messageList(barberUid: String): Flow<MutableList<Message>>
    suspend fun getOrder(): Flow<List<OrderModel>>
    suspend fun updateOrderStatus(order: OrderModel, status: String): Flow<Resource<String>>
    suspend fun getReview(barberuid:String):Flow<List<ReviewModel>>
    suspend fun updateSlotTimes(
        day: String,
        newStartTime: String,
        newEndTime: String
    ): Flow<Resource<String>>
}