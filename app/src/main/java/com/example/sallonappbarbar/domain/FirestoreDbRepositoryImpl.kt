package com.example.sallonappbarbar.domain

import android.content.ContentValues.TAG
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.SlotStatus
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.data.model.ServiceUploaded
import com.example.sallonappbarbar.data.model.Slots
import com.example.sallonappbarbar.data.model.TimeSlot
import com.example.sallonappbarbar.data.model.WorkDay
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class FirestoreDbRepositoryImpl @Inject constructor(
    private val barberdb: CollectionReference,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context

) : FirestoreRepository {

//    override fun insert(item: BarberModelResponse.BarberModelItem): Flow<Resource<String>> = callbackFlow{
//        trySend(Resource.Loading)
//        db.collection("barber")
//            .add(item)
//            .addOnSuccessListener {
//                trySend(Resource.Success("Data is inserted with ${it.id}"))//id is id of document at firestore
//            }.addOnFailureListener {
//                trySend(Resource.Failure(it))
//            }
//        awaitClose {
//            close()
//        }
//    }
// is it necessary to getItems and other function , should we allow the barbar to modify his data , i think we can use them
// in some way
//    override fun getItems(): Flow<Resource<List<BarberModelResponse>>> =  callbackFlow{
//        trySend(Resource.Loading)
//        db.collection("barber")
//            .get()
//            .addOnSuccessListener {
//                val items =  it.map { data->
//                    BarberModelResponse(
//                        item = BarberModelResponse.BarberModelItem(
//                            name = data.getString("name"),
//                            shopName = data.getString("shopName"),
//                            phoneNumber = data.getString("phoneNumber"),
//                            saloonType = data.getString("saloonType"),
//                            imageUris = data.get("imageUris") as List<String?>,
//                            shopAddress = data.getString("shopAddress")
//                        ),
//                        key = data.id
//                    )
//                }
//                trySend(Resource.Success(items))
//            }.addOnFailureListener {
//                trySend(Resource.Failure(it))
//            }
//
//        awaitClose {
//            close()
//        }
//    }

//    override fun delete(key: String): Flow<Resource<String>> = callbackFlow{
//        trySend(Resource.Loading)
//        db.collection("barber")
//            .document(key)
//            .delete()
//            .addOnCompleteListener {
//                if(it.isSuccessful)
//                    trySend(Resource.Success("Deleted successfully.."))
//            }.addOnFailureListener {
//                trySend(Resource.Failure(it))
//            }
//        awaitClose {
//            close()
//        }
//    }

    //    override fun update(item: BarberModelResponse): Flow<Resource<String>> = callbackFlow{
//        trySend(Resource.Loading)
//        val map = HashMap<String,Any>()
//        map["name"] = item.item.name!!
//        map["shopName"] = item.item.shopName!!
//        map["phoneNumber"] = item.item.phoneNumber!!
//        map["saloonType"] = item.item.saloonType!!
//        map["imageUris"] = item.item.imageUris
//        map["shopAddress"] = item.item.shopAddress!!
//
//        db.collection("barber")
//            .document(item.key!!)
//            .update(map)
//            .addOnCompleteListener {
//                if(it.isSuccessful)
//                    trySend(Resource.Success("Update successfully..."))
//            }.addOnFailureListener {
//                trySend(Resource.Failure(it))
//            }
//        awaitClose {
//            close()
//        }
//    }
    override suspend fun addUser(barberModel: BarberModel, imageUri: Uri?): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading)
            CoroutineScope(Dispatchers.IO).launch {
                if (imageUri != null) {
                    val storageRef =
                        storage.reference.child("profile_image/${auth.currentUser?.uid}.jpg")
                    storageRef.putFile(imageUri).addOnCompleteListener { task ->
                        storageRef.downloadUrl.addOnCompleteListener { imageUri ->
                            val downloadImage = imageUri.result

                            barberModel.imageUri = downloadImage.toString()
                        }
                    }.await()
                } else {
                    barberModel.imageUri =
                        "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/salon_app_logo.png?alt=media&token=0909deb8-b9a8-415a-b4b6-292aa2729636"
                }

                delay(1000)
                barberdb.document(auth.currentUser?.uid.toString())
                    .set(barberModel)
                    .addOnSuccessListener {
                        trySend(Resource.Success("Successfully Sign In"))
                    }.addOnFailureListener {
                        trySend(Resource.Failure(it))
                    }
            }
            awaitClose {
                close()
            }
        }

    override suspend fun addServices(
        aServices: List<ServiceType>
    ): Flow<Resource<String>> = callbackFlow {
        var service = HashMap<String, ServiceUploaded>()
        trySend(Resource.Loading)
        aServices.forEach(){
            ServiceType->
            ServiceType.aServices.forEach(){
                service[it.serviceName] = ServiceUploaded(it.price,it.time)
            }
            barberdb.document(auth.currentUser!!.uid).collection("Services").document(ServiceType.serviceTypeHeading).set(service)
                .addOnSuccessListener {
                    trySend(Resource.Success("Successfully added services"))
                }.addOnFailureListener {
                    trySend(Resource.Failure(it))
                    Log.d("TAGerr", "addServices: ${it.message}")
                }
            service.clear()

        }
        awaitClose {
            close()
        }
    }
    override suspend fun isShopOpen(shopOpen: Boolean): Flow<Resource<String>> = callbackFlow {
        try {
            trySend(Resource.Loading)
            barberdb.document(auth.currentUser?.uid.toString()).update("open", shopOpen)
                .await()
            trySend(Resource.Success("Status updated successfully"))
        } catch (e: Exception) {
            trySend(Resource.Failure(e))
        } finally {
            close()
        }
    }

override suspend fun addOpenCloseTime(openCloseTime: String): Flow<Resource<String>> = callbackFlow {
    trySend(Resource.Loading)
    barberdb.document(auth.currentUser?.uid.toString()).set(hashMapOf("openCloseTime" to openCloseTime)
    ,SetOptions.merge()
        )
        .addOnSuccessListener {
            trySend(Resource.Success("Successfully added open close time"))
        }.addOnFailureListener {
            trySend(Resource.Failure(it))
        }
    awaitClose {
        close()
    }
}
    private fun generateTimeSlots(startTime: String, endTime: String): List<String> {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val start = LocalTime.parse(startTime, formatter)
        val end = if (endTime == "00:00") LocalTime.MAX else LocalTime.parse(endTime, formatter)
        val slots = mutableListOf<String>()
        var current = start

        while (current.isBefore(end) || (end == LocalTime.MAX && current.isBefore(LocalTime.MIDNIGHT))) {
            slots.add(current.format(formatter))
            current = current.plusMinutes(30)
            if (current == LocalTime.MIDNIGHT) {
                break
            }
        }
        if (end == LocalTime.MAX) {
            slots.add("00:00")
        }
        return slots
    }
    override suspend fun setSlots(openCloseTime: List<Slots>): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            openCloseTime.forEach { slot ->
                try {
                    barberdb.document(auth.currentUser!!.uid).collection("Slots").document(slot.day)
                        .set(slot).await()
                    trySend(Resource.Success("All data uploaded successfully")).isSuccess
                } catch (e: Exception) {
                    trySend(Resource.Failure(e)).isSuccess
                }
            }
        }

        awaitClose { close() }
    }

    override suspend fun updateBookedSlots(
        times: List<String>,
        day: String
    ): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            val docRef = barberdb.document(auth.currentUser?.uid.toString()).collection("Slots")
                .document(day)
            docRef.get().addOnSuccessListener { document ->
                val currentBookedSlots =
                    document.get("booked") as? List<String> ?: emptyList()
                val mergedSlots = currentBookedSlots.toMutableList().apply {
                    addAll(times)
                    distinct()
                }
                docRef.update("booked", mergedSlots)
                    .addOnSuccessListener {
                        trySend(Resource.Success("Successfully updated booked slots $day"))
                    }.addOnFailureListener {
                        trySend(Resource.Failure(it))
                    }.addOnFailureListener {
                        trySend(Resource.Failure(it))
                    }
            } }catch (e: Exception) {
            trySend(Resource.Failure(e))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun updateNotAvailableSlots(
        times: List<String>,
        day: String
    ): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            val docRef = barberdb.document(auth.currentUser?.uid.toString()).collection("Slots")
                .document(day)
            docRef.get().addOnSuccessListener { document ->
                val currentNotAvailableSlots =
                    document.get("notAvailable") as? List<String> ?: emptyList()
                val mergedSlots = currentNotAvailableSlots.toMutableList().apply {
                    addAll(times)
                    distinct()
                }
                docRef.update("notAvailable", mergedSlots)
                    .addOnSuccessListener {
                        trySend(Resource.Success("Successfully updated not available slots $day"))
                    }.addOnFailureListener {
                        trySend(Resource.Failure(it))
                    }.addOnFailureListener {
                        trySend(Resource.Failure(it))
                    }
            } }catch (e: Exception) {
                trySend(Resource.Failure(e))
            }
            awaitClose {
                close()
            }
        }

    override suspend fun getTimeSlot(day: String, uid: String): Slots {
        return withContext(Dispatchers.IO) {
            try {
                val documentSnapshot = barberdb.document(auth.currentUser?.uid.toString()).
                collection("Slots").document(day).get().await()
                val slots = documentSnapshot.let { document->
                    Slots(
                        StartTime = document.getString("startTime").toString(),
                        EndTime = document.getString("endTime").toString(),
                        Booked = document.get("booked") as? List<String>?: emptyList(),
                        NotAvailable = document.get("notAvailable") as? List<String>?: emptyList(),
                        date = document.getString("date").toString()
                    )
                }
                slots
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching time slots: ${e.message}", e)
                throw e
            }
        }
    }

//    fun createSlot(startTime: String, endTime: String? = null): TimeSlot {
//    }

//    fun createSlot(startTime: String, endTime: String? = null): TimeSlots {
//        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//        val start = LocalTime.parse(startTime, formatter)
//        val end = endTime?.let { LocalTime.parse(it, formatter) } ?: start.plus(30, ChronoUnit.MINUTES)
//
//     return TimeSlot(
//        return TimeSlots(
//            startTime = start.format(formatter),
//            endTime = end.format(formatter)
//        )
//    }
    override suspend fun getBarberData(): Flow<Resource<BarberModel>> = callbackFlow {
        trySend(Resource.Loading)
        val documentReference = barberdb.document(auth.currentUser?.uid.toString())
        val listener = documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    if (data != null) {
                        try {
                            val barberModel = BarberModel(
                                name = data["name"]?.toString().orEmpty(),
                                shopName = data["shopName"]?.toString().orEmpty(),
                                phoneNumber = data["phoneNumber"]?.toString().orEmpty(),
                                saloonType = data["saloonType"]?.toString().orEmpty(),
                                imageUri = data["imageUri"]?.toString().orEmpty(),
                                shopStreetAddress = data["shopStreetAddress"]?.toString().orEmpty(),
                                city = data["city"]?.toString().orEmpty(),
                                state = data["state"]?.toString().orEmpty(),
                                aboutUs = data["aboutUs"]?.toString().orEmpty(),
                                noOfReviews = data["noOfReviews"]?.toString().orEmpty(),
                                open = data["open"]?.toString().toBoolean(),
                                rating = data["rating"]?.toString()?.toDoubleOrNull() ?: 0.0,
                                lat = data["lat"]?.toString()?.toDoubleOrNull() ?: 0.0,
                                long = data["long"]?.toString()?.toDoubleOrNull() ?: 0.0,
                                uid = data["uid"]?.toString().orEmpty()
                            )
                            trySend(Resource.Success(barberModel))
                        } catch (e: Exception) {
                            trySend(Resource.Failure(e))
                        }
                    } else {
                        trySend(Resource.Failure(Exception("Data is null")))
                    }
                } else {
                    trySend(Resource.Failure(Exception("Document does not exist")))
                }
            }
            .addOnFailureListener {
                trySend(Resource.Failure(it))
            }

        awaitClose { // No listeners to remove, but awaitClose is necessary
            // Optional: Clean up any resources if needed
        }
    }
    override suspend fun getOpenCloseTime(): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)

        val documentReference = barberdb.document(auth.currentUser?.uid.toString())
        val listener = documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    if (data != null) {
                        try {
                            val openCloseTime = data["openCloseTime"]?.toString().orEmpty()
                            trySend(Resource.Success(openCloseTime))
                        } catch (e: Exception) {
                            trySend(Resource.Failure(e))
                        }
                    } else {
                        trySend(Resource.Failure(Exception("Data is null")))
                    }
                } else {
                    trySend(Resource.Failure(Exception("Document does not exist")))
                }
            }
            .addOnFailureListener {
                trySend(Resource.Failure(it))
            }

        awaitClose { // No listeners to remove, but awaitClose is necessary
            // Optional: Clean up any resources if needed
        }
    }
    override suspend fun getBarberSlots(): Flow<Resource<List<WorkDay>>> = callbackFlow {
        trySend(Resource.Loading)

        val documentReference = barberdb.document(auth.currentUser?.uid.toString())
        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    if (data != null) {
                        try {
                            val slotsMap = data["Slots available"] as? Map<String, List<Map<String, String>>>
                            if (slotsMap != null) {
                                val workDaySlots = slotsMap.mapNotNull { (day, slots) ->
                                    if (day is String && slots is List<*>) {
                                        val timeSlots = slots.mapNotNull { slotMap ->
                                            if (slotMap is Map<*, *>) {
                                                val startTime = slotMap["startTime"] as? String ?: ""
                                                val endTime = slotMap["endTime"] as? String ?: ""
                                                TimeSlot(startTime,"20- 5-50", SlotStatus.AVAILABLE)
                                            } else null
                                        }.toMutableStateList()
                                        WorkDay(day, timeSlots)
                                    } else null
                                }
                                trySend(Resource.Success(workDaySlots))
                            } else {
                                trySend(Resource.Failure(Exception("Slots data is not in the expected format")))
                            }
                        } catch (e: Exception) {
                            trySend(Resource.Failure(e))
                        }
                    } else {
                        trySend(Resource.Failure(Exception("Data is null")))
                    }
                } else {
                    trySend(Resource.Failure(Exception("Document does not exist")))
                }
            }
            .addOnFailureListener { exception ->
                trySend(Resource.Failure(exception))
            }

        awaitClose { // No listeners to remove, but awaitClose is necessary
            // Optional: Clean up any resources if needed
        }
    }
//    override suspend fun getBarberSlots(): Flow<Resource<List<WorkDay>>> = callbackFlow {
//        trySend(Resource.Loading)
//
//        val documentReference = barberdb.document(auth.currentUser?.uid.toString())
//        documentReference.get()
//            .addOnSuccessListener { documentSnapshot ->
//                if (documentSnapshot.exists()) {
//                    val data = documentSnapshot.data
//                    if (data != null) {
//                        try {
//                            val slotsMap = data["Slots available"] as? Map<String, List<Map<String, String>>>
//                            if (slotsMap != null) {
//                                val workDaySlots = slotsMap.mapNotNull { (day, slots) ->
//                                    if (day is String && slots is List<*>) {
//                                        val timeSlots = slots.mapNotNull { slotMap ->
//                                            if (slotMap is Map<*, *>) {
//                                                val startTime = slotMap["startTime"] as? String ?: ""
//                                                val endTime = slotMap["endTime"] as? String ?: ""
//                                                TimeSlots(startTime, endTime)
//                                            } else null
//                                        }.toMutableStateList()
//                                        WorkDay(day, timeSlots)
//                                    } else null
//                                }
//                                trySend(Resource.Success(workDaySlots))
//                            } else {
//                                trySend(Resource.Failure(Exception("Slots data is not in the expected format")))
//                            }
//                        } catch (e: Exception) {
//                            trySend(Resource.Failure(e))
//                        }
//                    } else {
//                        trySend(Resource.Failure(Exception("Data is null")))
//                    }
//                } else {
//                    trySend(Resource.Failure(Exception("Document does not exist")))
//                }
//            }
//            .addOnFailureListener { exception ->
//                trySend(Resource.Failure(exception))
//            }
//
//        awaitClose { // No listeners to remove, but awaitClose is necessary
//            // Optional: Clean up any resources if needed
//        }
//    }
}