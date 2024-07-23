package com.example.sallonappbarbar.domain

import android.content.ContentValues.TAG
import com.example.sallonappbarbar.data.FireStoreDbRepository
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
import com.example.sallonappbarbar.appUi.viewModel.OrderStatus
import com.example.sallonappbarbar.data.model.ChatModel
import com.example.sallonappbarbar.data.model.Message
import com.example.sallonappbarbar.data.model.OrderModel
import com.example.sallonappbarbar.data.model.ServiceCat
import com.example.sallonappbarbar.data.model.ServiceUploaded
import com.example.sallonappbarbar.data.model.Slots
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Named


class FirestoreDbRepositoryImpl @Inject constructor(
    @Named("UserData")
    private val usersDb: CollectionReference,
    private val storage: FirebaseStorage,
    @Named("BarberData")
    private val barberDb: CollectionReference,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context

) : FireStoreDbRepository {
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
                barberDb.document(auth.currentUser?.uid.toString())
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
        services: List<ServiceCat>
    ): Flow<Resource<String>> = callbackFlow {
        var service = HashMap<String, ServiceUploaded>()
        trySend(Resource.Loading)
        services.forEach(){
            ServiceType->
            ServiceType.services.forEach(){
                service[it.serviceName] = ServiceUploaded(it.price,it.time)
            }
            barberDb.document(auth.currentUser!!.uid).collection("Services").document(ServiceType.type).set(service)
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
        Log.d("slot",openCloseTime.toString())

        CoroutineScope(Dispatchers.IO).launch {
            openCloseTime.forEach { slot ->
                try {
                    barberDb.document(auth.currentUser!!.uid).collection("Slots").document(slot.day)
                        .set(slot).await()
                    Log.d("day",slot.day)
                } catch (e: Exception) {
                    trySend(Resource.Failure(e))
                }
            }
            trySend(Resource.Success("All data uploaded successfully")).isSuccess
        }

        awaitClose { close() }
    }

    override suspend fun updateBookedSlots(
        times: List<String>,
        day: String
    ): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            val docRef = barberDb.document(auth.currentUser?.uid.toString()).collection("Slots")
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
            val docRef = barberDb.document(auth.currentUser?.uid.toString()).collection("Slots")
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

    override suspend fun getTimeSlot(): List<Slots> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = barberDb.document(auth.currentUser?.uid.toString()).
                collection("Slots").get().await()
                val listOfSlots = querySnapshot.documents.map { document->
                    Slots(

                            startTime = document.getString("startTime").toString(),
                        endTime = document.getString("endTime").toString(),
                        booked = (document.get("booked") as? MutableList<String>) ?: mutableListOf(),
                        notAvailable = (document.get("notAvailable") as? MutableList<String>) ?: mutableListOf(),
                        date = document.getString("date").toString(),
                            day =document.getString("day").toString()

                    )

                }
                listOfSlots
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching time slots: ${e.message}", e)
                throw e
            }
        }
    }
    override suspend fun getBarber(uid: String?): BarberModel? {
        return withContext(Dispatchers.IO) {
            val querySnapshot =
                barberDb.whereEqualTo("uid", uid)
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
        val documentReference = barberDb.document(auth.currentUser?.uid.toString())
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
    override suspend fun addChat(message: Message, useruid: String) {
        try {
            Firebase.firestore.collection("Chats").document("$useruid${auth.currentUser?.uid}")
                .set(
                    mapOf(
                        "barberuid" to auth.currentUser?.uid.toString(),
                        "useruid" to useruid,
                        "lastmessage" to message
                    )
                ).await()
            Firebase.firestore.collection("Chats").document("${auth.currentUser?.uid}$useruid")
                .collection("Messages").document(message.time).set(message).await()
        } catch (e: Exception) {
            Log.d("chat", "Error adding chat: ${e.message}")
        }
    }

    override suspend fun getChatBarber(): MutableList<ChatModel> {
        return withContext(Dispatchers.IO) {
            val querySnapshot = Firebase.firestore.collection("Chats")
                .whereEqualTo("barberuid", auth.currentUser?.uid.toString()).get().await()
            val chatList = querySnapshot.documents.map { documentSnapshot ->
                val userDocument =
                    usersDb.document(documentSnapshot.getString("useruid").toString()).get()
                        .await()
                val name = userDocument.getString("name").toString()
                val image = userDocument.getString("imageUri")
                    .toString() // Assuming "image" field contains the URL of the image
                val phoneNumber = userDocument.getString("phoneNumber").toString()
                val message = documentSnapshot.get("lastmessage") as Map<*, *>
                val lastMessage = Message(
                    message = message["message"].toString(),
                    time = message["time"].toString(),
                    status = message["status"].toString().toBoolean()
                )
                ChatModel(
                    name = name,
                    image = image, // Add the image URL to ChatModel
                    message = lastMessage,
                    uid=documentSnapshot.getString("useruid").toString(),
                    phoneNumber = phoneNumber
                )
            }.toMutableList()
            Log.d("userchat", "$chatList")
            chatList
        }
    }

    override suspend fun messageList(userUid: String): Flow<List<Message>> = callbackFlow {
        val messageRef = Firebase.firestore.collection("Chats")
            .document("$userUid${auth.currentUser?.uid}")
            .collection("Messages")

        val subscription = messageRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                trySend(emptyList()) // Send an empty list on error
                return@addSnapshotListener
            }

            val messageList = querySnapshot?.documents?.map { documentSnapshot ->
                Message(
                    message = documentSnapshot.getString("message").toString(),
                    time = documentSnapshot.getString("time").toString(),
                    status = documentSnapshot.getBoolean("status")!!
                )
            } ?: emptyList()

            trySend(messageList)
        }
        awaitClose { subscription.remove() }
    }
    override suspend fun getOrders(onOrderUpdate: (List<OrderModel>) -> Unit) {
        Firebase.firestore.collection("booking")
            .whereEqualTo("barberuid", auth.currentUser?.uid.toString())
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("FireStoreDbRepository", "listen:error", e)
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    val orders = mutableListOf<OrderModel>()
                    val scope = CoroutineScope(Dispatchers.IO)
                    scope.launch {
                        for (documentSnapshot in snapshots.documents) {
                            val orderId = documentSnapshot.id
                            val serviceNames = mutableListOf<String>()
                            val serviceTypes = mutableListOf<String>()
                            val timesList = mutableListOf<String>()
                            val userDocument = usersDb
                                .document(documentSnapshot.getString("useruid").toString()).get().await()
                            val name = userDocument.getString("name").toString()
                            val image = userDocument.getString("imageUri").toString()
                            val phoneNo = userDocument.getString("phoneNumber").toString()
                            val services = documentSnapshot.get("service") as? List<Map<*, *>> ?: emptyList()
                            for (service in services) {
                                serviceNames.add(service["serviceName"].toString())
                                serviceTypes.add(service["type"].toString())
                            }
                            val times = documentSnapshot.get("times") as? List<Map<String, Any>> ?: emptyList()
                            for (time in times) {
                                timesList.add(time["time"].toString())
                            }
                            val orderDate = documentSnapshot.getString("date").toString()
                            val paymentMethod = if (documentSnapshot.contains("paymentMethod")) {
                                documentSnapshot.getString("paymentMethod").toString()
                            } else {
                                "Cash"
                            }
                            val orderStatus = when (documentSnapshot.getString("status").toString().lowercase()) {
                                "declined" -> OrderStatus.DECLINED
                                "completed" -> OrderStatus.COMPLETED
                                "accepted" -> OrderStatus.ACCEPTED
                                else -> OrderStatus.PENDING
                            }
                            val orderModel = OrderModel(
                                imageUrl = image,
                                orderType = serviceNames,
                                timeSlot = timesList,
                                phoneNumber = phoneNo,
                                customerName = name,
                                paymentMethod = paymentMethod,
                                orderStatus = orderStatus,
                                orderId = orderId,
                                date = orderDate
                            )
                            orders.add(orderModel)
                        }
                        withContext(Dispatchers.Main) {
                            onOrderUpdate(orders)
                        }
                    }
                }
            }
    }

    override suspend fun updateOrderStatus(orderId: String, status: String):Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            Firebase.firestore.collection("booking").document(orderId)
                .update("status", status)
                .addOnSuccessListener {
                    trySend(Resource.Success("Order status updated successfully"))
                }.addOnFailureListener {
                    trySend(Resource.Failure(it))
                }
        } catch (e: Exception) {
            trySend(Resource.Failure(e))
        }
        awaitClose {
            close()
        }
    }

}