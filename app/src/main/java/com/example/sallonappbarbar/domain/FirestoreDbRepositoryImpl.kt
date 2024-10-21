package com.example.sallonappbarbar.domain

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.sallonappbarbar.appUi.viewModel.OrderStatus
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.BookedModel
import com.example.sallonappbarbar.data.model.ChatModel
import com.example.sallonappbarbar.data.model.LastMessage
import com.example.sallonappbarbar.data.model.Message
import com.example.sallonappbarbar.data.model.OrderModel
import com.example.sallonappbarbar.data.model.ReviewModel
import com.example.sallonappbarbar.data.model.ServiceCat
import com.example.sallonappbarbar.data.model.ServiceModel
import com.example.sallonappbarbar.data.model.ServiceUploaded
import com.example.sallonappbarbar.data.model.Slots
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
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
    override suspend fun addBarber(
        barberModel: BarberModel,
        imageUri: Uri?
    ): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading)
            CoroutineScope(Dispatchers.IO).launch {
                if (imageUri != null) {
                    val storageRef =
                        storage.reference.child("profile_image/${auth.currentUser?.uid}.jpg")
                    storageRef.delete()
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

    override suspend fun updateBarberInfo(
        barberModel: BarberModel,
        imageUri: Uri?
    ): Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            if (imageUri != null) {
                val storageRef =
                    storage.reference.child("profile_image/${auth.currentUser?.uid}$.jpg")

                storageRef.delete()
                storageRef.putFile(imageUri).await()
                val downloadImage = storageRef.downloadUrl.await()
                barberModel.imageUri = downloadImage.toString()

            }
            delay(500)
            val updates = mutableMapOf<String, Any?>()

            barberModel.name?.takeIf { it.isNotEmpty() }?.let { updates["name"] = it }
            barberModel.shopName?.takeIf { it.isNotEmpty() }?.let { updates["shopName"] = it }
            barberModel.phoneNumber?.takeIf { it.isNotEmpty() }?.let { updates["phoneNumber"] = it }
            barberModel.saloonType?.takeIf { it.isNotEmpty() }?.let { updates["saloonType"] = it }
            barberModel.shopStreetAddress?.takeIf { it.isNotEmpty() }
                ?.let { updates["shopStreetAddress"] = it }
            barberModel.city?.takeIf { it.isNotEmpty() }?.let { updates["city"] = it }
            barberModel.state?.takeIf { it.isNotEmpty() }?.let { updates["state"] = it }
            barberModel.aboutUs?.takeIf { it.isNotEmpty() }?.let { updates["aboutUs"] = it }
            barberModel.noOfReviews?.takeIf { it.isNotEmpty() }?.let { updates["noOfReviews"] = it }
            barberModel.imageUri?.takeIf { it.isNotEmpty() }?.let { updates["imageUri"] = it }
            barberModel.open?.let { updates["open"] = it }
            barberModel.rating?.let { updates["rating"] = it }
            barberModel.lat?.let { updates["lat"] = it }
            barberModel.long?.let { updates["long"] = it }

            barberDb.document(auth.currentUser?.uid.toString())
                .update(updates)
                .addOnSuccessListener {
                    trySend(Resource.Success("Successfully Updated"))
                }.addOnFailureListener { e ->
                    trySend(Resource.Failure(e))
                }

        }
        awaitClose { close() }
    }

    override suspend fun addServices(
        services: List<ServiceCat>
    ): Flow<Resource<String>> = callbackFlow {
        var service = HashMap<String, ServiceUploaded>()
        trySend(Resource.Loading)
        services.forEach { ServiceType ->
            ServiceType.services.forEach {
                service[it.serviceName] = ServiceUploaded(it.price, it.time, it.id)
            }
            barberDb.document(auth.currentUser!!.uid).collection("Services")
                .document(ServiceType.type).set(service)
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

    override suspend fun getServices(): Flow<Resource<List<ServiceCat>>> = callbackFlow {
        trySend(Resource.Loading)

        barberDb.document(auth.currentUser!!.uid).collection("Services")
            .get()
            .addOnSuccessListener { documents ->
                val serviceCats = mutableListOf<ServiceCat>()

                for (document in documents) {
                    val type = document.id
                    val servicesMap = document.data as? Map<String, HashMap<String, Any>>

                    if (servicesMap != null) {
                        val services = servicesMap.map { (serviceName, serviceData) ->
                            val id = when (val rawId = serviceData["id"]) {
                                is String -> rawId.toIntOrNull() ?: 0
                                is Long -> rawId.toInt()
                                is Int -> rawId
                                is Double -> rawId.toInt()
                                else -> 0
                            }
                            ServiceModel(
                                serviceName = serviceName,
                                id = id,
                                price = serviceData["servicePrice"] as? String ?: "0",
                                time = serviceData["serviceDuration"] as? String ?: "00:00",
                                type = type,
                                isServiceSelected = true
                            )
                        }
                        serviceCats.add(ServiceCat(type = type, services = services))
                    }
                }
                trySend(Resource.Success(serviceCats))
                Log.d("TAG", "getServices: $serviceCats")
            }
            .addOnFailureListener { exception ->
                trySend(Resource.Failure(exception))
                Log.d("TAGerr", "getServices: ${exception.message}")
            }

        awaitClose {
            close()
        }
    }

    override suspend fun setSlots(openCloseTime: List<Slots>): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading)
            Log.d("slot", openCloseTime.toString())

            CoroutineScope(Dispatchers.IO).launch {
                openCloseTime.forEach { slot ->
                    try {
                        barberDb.document(auth.currentUser!!.uid).collection("Slots")
                            .document(slot.day)
                            .set(slot).await()
                        Log.d("day", slot.day)
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
            }
        } catch (e: Exception) {
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
            }
        } catch (e: Exception) {
            trySend(Resource.Failure(e))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun getTimeSlot(): List<Slots> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot =
                    barberDb.document(auth.currentUser?.uid.toString()).collection("Slots").get()
                        .await()
                val listOfSlots = querySnapshot.documents.map { document ->
                    Slots(

                        startTime = document.getString("startTime").toString(),
                        endTime = document.getString("endTime").toString(),
                        booked = (document.get("booked") as? MutableList<String>)
                            ?: mutableListOf(),
                        notAvailable = (document.get("notAvailable") as? MutableList<String>)
                            ?: mutableListOf(),
                        date = document.getString("date").toString(),
                        day = document.getString("day").toString()

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

    override suspend fun addChat(message: LastMessage, useruid: String, status: Boolean) {
        try {
            Firebase.firestore.collection("Chats").document("${auth.currentUser?.uid}${useruid}")
                .set(
                    mapOf(
                        "barberuid" to auth.currentUser?.uid.toString(),
                        "useruid" to useruid,
                        "lastmessage" to message,
                    )
                ).await()
            if (status) {
                Firebase.firestore.collection("Chats").document("${auth.currentUser?.uid}$useruid")
                    .collection("Messages").document(message.time).set(message).await()
            }
        } catch (e: Exception) {
            Log.d("chat", "Error adding chat: ${e.message}")
        }
    }

    override suspend fun getChatBarber(): Flow<MutableList<ChatModel>> = callbackFlow {
        val listenerRegistration = Firebase.firestore.collection("Chats")
            .whereEqualTo("barberuid", auth.currentUser?.uid.toString())
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    launch {
                        val chatList = querySnapshot.documents.map { documentSnapshot ->
                            val userDocument =
                                usersDb.document(documentSnapshot.getString("useruid").toString())
                                    .get().await()
                            val name = userDocument.getString("name").toString()
                            val image = userDocument.getString("imageUri").toString()
                            val phoneNumber = userDocument.getString("phoneNumber").toString()
                            val message = documentSnapshot.get("lastmessage") as Map<*, *>
                            val lastMessage = LastMessage(
                                message = message["message"].toString(),
                                time = message["time"].toString(),
                                status = message["status"].toString().toBoolean(),
                                seenbybarber = message["seenbybarber"].toString().toBoolean(),
                                seenbyuser = message["seenbyuser"].toString().toBoolean()
                            )
                            ChatModel(
                                name = name,
                                image = image,
                                message = lastMessage,
                                uid = documentSnapshot.getString("useruid").toString(),
                                phoneNumber = phoneNumber
                            )
                        }.toMutableList()

                        trySend(chatList).isSuccess
                    }
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun messageList(userUid: String): Flow<MutableList<Message>> = callbackFlow {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -14) // Subtract 14 days from today
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val fourteenDaysAgo = calendar.time

        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())

        val messageRef = Firebase.firestore.collection("Chats")
            .document("${auth.currentUser?.uid}${userUid}")
            .collection("Messages")

        val subscription =
            messageRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    trySend(emptyList<Message>().toMutableList()) // Send an empty list on error
                    return@addSnapshotListener
                }

                val messageList = querySnapshot?.documents?.mapNotNull { documentSnapshot ->
                    val timeString = documentSnapshot.get("time").toString()
                    val orderDate = timeString.let { dateFormat.parse(it) }

                    if (orderDate != null && orderDate.after(fourteenDaysAgo)) {
                        Message(
                            message = documentSnapshot.getString("message").orEmpty(),
                            time = timeString,
                            status = documentSnapshot.getBoolean("status") ?: false
                        )
                    } else if (orderDate != null) {
                        documentSnapshot.reference.delete()
                        null
                    } else {
                        null // Skip messages older than 14 days or with invalid data
                    }
                }?.toMutableList() ?: mutableListOf()

                trySend(messageList)
            }

        awaitClose { subscription.remove() }
    }.flowOn(Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getOrder(): Flow<List<OrderModel>> = callbackFlow {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -8) // Subtract 8 days from today
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val eightDaysAgo = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val _result = mutableListOf<OrderModel>()

        // Listener for bookings
        val listenerRegistration = Firebase.firestore.collection("Booking")
            .whereEqualTo("barberuid", auth.currentUser?.uid.toString())
            .addSnapshotListener { bookingsSnapshot, e ->
                if (e != null) {
                    close(e) // Close flow on error
                    return@addSnapshotListener
                }

                if (bookingsSnapshot != null && !bookingsSnapshot.isEmpty) {
                    bookingsSnapshot.documents.forEach { bookingDoc ->
                        bookingDoc.reference.collection("Order")
                            .addSnapshotListener { orderSnapshot, error ->
                                if (error != null) {
                                    close(error) // Close flow on error
                                    return@addSnapshotListener
                                }
                                if (orderSnapshot != null && !orderSnapshot.isEmpty) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val jobs = orderSnapshot.documents.map { orderDoc ->
                                            async {
                                                val timeString =
                                                    orderDoc.get("selectedDate").toString()
                                                val orderDate =
                                                    timeString.let { dateFormat.parse(it) }

                                                if (orderDate != null && (orderDate.after(
                                                        eightDaysAgo
                                                    ) || orderDoc.getString("status") == "completed")
                                                ) {
                                                    val bookedModel =
                                                        orderDoc.toObject(BookedModel::class.java)
                                                    val userDocument = bookedModel?.let {
                                                        usersDb.document(it.useruid).get().await()
                                                    }

                                                    val name = userDocument?.getString("name")
                                                        .toString()
                                                    val image = userDocument?.getString("imageUri")
                                                        .toString()
                                                    val phoneNo =
                                                        userDocument?.getString("phoneNumber")
                                                            .toString()
                                                    val listOfService =
                                                        bookedModel?.listOfService ?: listOf()
                                                    val timeSlots =
                                                        bookedModel?.selectedSlots ?: listOf()
                                                    val review = bookedModel?.review as ReviewModel

                                                    val orderStatus = when (bookedModel.status) {
                                                        "completed" -> OrderStatus.COMPLETED
                                                        "accepted" -> OrderStatus.ACCEPTED
                                                        "cancelled" -> OrderStatus.CANCELLED
                                                        else -> OrderStatus.PENDING
                                                    }

                                                    // Create OrderModel and return it
                                                    val newOrder = OrderModel(
                                                        userImageUrl = image,
                                                        listOfService = listOfService,
                                                        timeSlot = timeSlots,
                                                        userPhoneNumber = phoneNo,
                                                        userName = name,
                                                        paymentMethod = "Cash",
                                                        orderStatus = orderStatus,
                                                        orderId = bookedModel.dateandtime,
                                                        date = bookedModel.selectedDate,
                                                        barberuid = bookedModel.barberuid,
                                                        useruid = bookedModel.useruid,
                                                        review = review,
                                                        genderCounter = bookedModel.genderCounter
                                                    )
                                                    val existingOrderIndex = _result.indexOfFirst {
                                                        it.orderId == bookedModel.dateandtime
                                                                && it.useruid==newOrder.useruid
                                                    }

                                                    // Add or update the order based on orderId and orderStatus
                                                    if (existingOrderIndex != -1) {
                                                        _result.removeAt(existingOrderIndex)

                                                    }
                                                    newOrder
                                                } else {
                                                    val status = orderDoc.getString("status")
                                                    if (status == "cancelled" || status == "pending" || status == "accepted") {
                                                        orderDoc.reference.delete()
                                                    }
                                                    null
                                                }
                                            }
                                        }

                                        // Wait for all jobs to finish and collect results
                                        val orders = jobs.awaitAll().filterNotNull()
                                        _result.addAll(orders)
                                        trySend(_result.sortedByDescending { it.orderId }).isSuccess
                                    }
                                }
                            }
                    }
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun updateOrderStatus(order: OrderModel, status: String)
            : Flow<Resource<String>> = callbackFlow {
        trySend(Resource.Loading)
        try {
            Firebase.firestore.collection("Booking").document(order.barberuid + order.useruid)
                .collection("Order").document(order.orderId)
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

    override suspend fun getReview(barberuid: String): Flow<List<ReviewModel>> =
        callbackFlow {
            val db = FirebaseFirestore.getInstance()
            val _result = mutableListOf<ReviewModel>()

            // Step 1: Query Bookings where userUid matches
            val listenerRegistration = db.collection("Booking")
                .whereEqualTo("barberuid", barberuid)
                .addSnapshotListener { bookingsSnapshot, e ->
                    if (e != null) {
                        close(e) // Close flow on error
                        return@addSnapshotListener
                    }
                    // Step 2: Set listeners on each order subcollection of the matching bookings
                    if (bookingsSnapshot != null && !bookingsSnapshot.isEmpty) {
                        bookingsSnapshot.documents.forEach { bookingDoc ->
                            bookingDoc.reference.collection("Order")
                                .addSnapshotListener { ordersSnapshot, error ->
                                    if (error != null) {
                                        close(error) // If there is an error, close the flow with the error
                                    } else if (ordersSnapshot != null) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            // Collect orders data from the snapshot and process them in parallel
                                            val jobs = ordersSnapshot.documents.map { order ->
                                                async {
                                                    // Convert Firestore document to BookedModel
                                                    val bookedModel =
                                                        order.toObject(BookedModel::class.java)
                                                    if (bookedModel?.review?.reviewTime?.isNotEmpty() == true
                                                    ) {
                                                        ReviewModel(
                                                            rating = bookedModel.review.rating,
                                                            reviewTime = bookedModel.review.reviewTime,
                                                            reviewText = bookedModel.review.reviewText,
                                                            userDp = bookedModel.review.userDp,
                                                            userName = bookedModel.review.userName
                                                        )
                                                    } else {
                                                        null
                                                    }
                                                }

                                            }
                                            val results = jobs.awaitAll().filterNotNull()
                                            _result.addAll(results)
                                            trySend(_result.sortedByDescending { it.rating }).isSuccess
                                        }
                                    }
                                }
                        }
                    }
                }
            awaitClose { listenerRegistration.remove() }
        }

    override suspend fun updateSlotTimes(
        day: String,
        newStartTime: String,
        newEndTime: String
    ): Flow<Resource<String>> =
        callbackFlow {
            trySend(Resource.Loading)
            try {
                val slotDocument = barberDb.document(auth.currentUser!!.uid)
                    .collection("Slots")
                    .document(day)

                val snapshot = slotDocument.get().await()
                if (snapshot.exists()) {
                    val slot = snapshot.toObject(Slots::class.java)
                    if (slot != null) {
                        slot.startTime = newStartTime
                        slot.endTime = newEndTime
                        slot.date = LocalDate.now().toString()
                        slotDocument.set(slot).await()
                        trySend(Resource.Success("Slot updated successfully"))
                    } else {
                        trySend(Resource.Failure(Exception("Slot not found")))
                    }
                } else {
                    trySend(Resource.Failure(Exception("Slot not found")))
                }
            } catch (e: Exception) {
                trySend(Resource.Failure(e))
            }
            awaitClose { close() }
        }
}