package com.example.sallonappbarbar.data


import android.net.Uri
import com.example.sallonappbarbar.data.model.BarberModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    suspend  fun addUser(
        barberModel: BarberModel,
        imageUri: Uri?
//        name:String,phoneNumber:String,dateOfBirth:String,gender:String,imageUri:String
    ) : Flow<Resource<String>>

}