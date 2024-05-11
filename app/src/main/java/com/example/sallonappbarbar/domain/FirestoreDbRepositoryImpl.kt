package com.example.sallonappbarbar.domain

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
import com.example.sallonappbarbar.data.model.ServiceType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


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
        trySend(Resource.Loading)
        aServices.forEach(){ServiceType->
            ServiceType.aServices.forEach(){
                val service = HashMap<String, Any>()
                service["Service Name"] = it.serviceName
                service["Service Price"] = it.price
                service["Service Duration"] = it.time

                barberdb.document(auth.currentUser!!.uid).collection("Services").document(ServiceType.serviceTypeHeading).set(service)
                    .addOnSuccessListener {
                        trySend(Resource.Success("Successfully added services"))
                    }.addOnFailureListener {
                        trySend(Resource.Failure(it))
                    }
            }
        }
        awaitClose {
            close()
        }
    }
    override suspend fun isShopOpen(shopOpen: Boolean): Flow<Resource<String>> = callbackFlow {
        var status = if (shopOpen) "Yes" else "No"
        trySend(Resource.Loading)
        barberdb.document(auth.currentUser?.uid.toString()).update("isShopOpen", status)
            .addOnSuccessListener {
                trySend(Resource.Success("Shop is open"))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override suspend fun getBarberData(): Flow<Resource<BarberModel>> = callbackFlow {
        trySend(Resource.Loading)
        barberdb.document(auth.currentUser?.uid.toString()).get()
            .addOnSuccessListener {
                val barberModel = it.data?.map {
                    BarberModel(
                        name = it.value.toString(),
                        shopName = it.value.toString(),
                        phoneNumber = it.value.toString(),
                        saloonType = it.value.toString(),
                        imageUri = it.value.toString(),
                        shopStreetAddress = it.value.toString(),
                        city = it.value.toString(),
                        state = it.value.toString(),
                        aboutUs = it.value.toString(),
                        noOfReviews = it.value.toString(),
                        isShopOpen = it.value.toString(),
                        rating = it.value.toString().toDouble(),
                        lat = it.value.toString().toDouble(),
                        long = it.value.toString().toDouble(),
                        uid = it.value.toString()
                    )
                }
                trySend(Resource.Success(barberModel!![0]))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        awaitClose {
            close()
        }
    }
}