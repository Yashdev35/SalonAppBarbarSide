package com.example.sallonappbarbar.domain

import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModelResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreDbRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : FirestoreRepository {

    override fun insert(item: BarberModelResponse.BarberModelItem): Flow<Resource<String>> = callbackFlow{
        trySend(Resource.Loading)
        db.collection("barber")
            .add(item)
            .addOnSuccessListener {
                trySend(Resource.Success("Data is inserted with ${it.id}"))//id is id of document at firestore
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        awaitClose {
            close()
        }
    }
// is it necessary to getItems and other function , should we allow the barbar to modify his data , i think we can use them
// in some way
    override fun getItems(): Flow<Resource<List<BarberModelResponse>>> =  callbackFlow{
        trySend(Resource.Loading)
        db.collection("barber")
            .get()
            .addOnSuccessListener {
                val items =  it.map { data->
                    BarberModelResponse(
                        item = BarberModelResponse.BarberModelItem(
                            name = data.getString("name"),
                            shopName = data.getString("shopName"),
                            phoneNumber = data.getString("phoneNumber"),
                            saloonType = data.getString("saloonType"),
                            imageUris = data.get("imageUris") as List<String?>,
                            shopAddress = data.getString("shopAddress")
                        ),
                        key = data.id
                    )
                }
                trySend(Resource.Success(items))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun delete(key: String): Flow<Resource<String>> = callbackFlow{
        trySend(Resource.Loading)
        db.collection("barber")
            .document(key)
            .delete()
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(Resource.Success("Deleted successfully.."))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun update(item: BarberModelResponse): Flow<Resource<String>> = callbackFlow{
        trySend(Resource.Loading)
        val map = HashMap<String,Any>()
        map["name"] = item.item.name!!
        map["shopName"] = item.item.shopName!!
        map["phoneNumber"] = item.item.phoneNumber!!
        map["saloonType"] = item.item.saloonType!!
        map["imageUris"] = item.item.imageUris
        map["shopAddress"] = item.item.shopAddress!!

        db.collection("barber")
            .document(item.key!!)
            .update(map)
            .addOnCompleteListener {
                if(it.isSuccessful)
                    trySend(Resource.Success("Update successfully..."))
            }.addOnFailureListener {
                trySend(Resource.Failure(it))
            }
        awaitClose {
            close()
        }
    }
}