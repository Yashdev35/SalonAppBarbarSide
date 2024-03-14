package com.example.sallonappbarbar.data


import com.example.sallonappbarbar.data.model.BarberModelResponse
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {

    fun insert(
        item:BarberModelResponse.BarberModelItem
    ) : Flow<Resource<String>>

    fun getItems() : Flow<Resource<List<BarberModelResponse>>>

    fun delete(key:String) : Flow<Resource<String>>

    fun update(
        item:BarberModelResponse
    ) : Flow<Resource<String>>

}