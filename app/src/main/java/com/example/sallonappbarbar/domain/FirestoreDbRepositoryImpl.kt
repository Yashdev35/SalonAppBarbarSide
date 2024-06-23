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
                        Booked = (document.get("booked") as? List<*>)?.filterIsInstance<String>()?.toMutableList() ?: mutableListOf(),
                        NotAvailable = (document.get("notAvailable") as? List<*>)?.filterIsInstance<String>()?.toMutableList() ?: mutableListOf(),
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
    override suspend fun getBarber(uid: String?): BarberModel? {
        return withContext(Dispatchers.IO) {
            val querySnapshot =
                barberdb.whereEqualTo("uid", uid)
                    .limit(1).get().await()
            val barberDocument = querySnapshot.documents.firstOrNull()
            barberDocument?.let { document ->
                BarberModel(
                    name = document.getString("name") ?: "",
                    rating = document.getDouble("rating") ?: 0.0,
                    shopName = document.getString("shopName") ?: "",
                    imageUri = document.getString("imageUri")
                        ?: "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/profile_image%2FptNTxkdC31NBaSQbJT5cnQQHO2u2.jpg?alt=media&token=ed411c18-99ad-4db2-94ab-23e1c9b2b1b6",
                    shopStreetAddress = document.getString("shopStreetAddress") ?: "",
                    phoneNumber = document.getString("phoneNumber") ?: "",
                    city = document.getString("city") ?: "",
                    uid = document.getString("uid").toString(),
                    noOfReviews = document.getString("noOfReviews"),
                    state = document.getString("state").toString(),
                    aboutUs = document.getString("aboutUs").toString(),
                    lat = document.getDouble("lat")!!.toDouble(),
                    long = document.getDouble("long")!!.toDouble(),
                    open = document.getBoolean("open")!!,
                    )
            }
        }
    }
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

        awaitClose { }
    }
}