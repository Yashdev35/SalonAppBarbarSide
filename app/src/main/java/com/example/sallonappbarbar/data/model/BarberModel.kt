package com.example.sallonappbarbar.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarberModel(
        val name: String? = "",
        val shopName: String? = "",
        val phoneNumber: String? = "",
        val saloonType: String? = "",
        var imageUri: String? = "",
        val shopStreetAddress: String? = "",
        var city: String? = "",
        var state: String? = "",
        var aboutUs: String? = "",
        var noOfReviews: String? = "",
        var open: Boolean? = false,
        var rating: Double? =0.0,
        var lat:Double? = 0.0,
        var long:Double? =0.0,
        val uid:String? = "",
        //var aServices:List<aService>? = emptyList()
    ): Parcelable



