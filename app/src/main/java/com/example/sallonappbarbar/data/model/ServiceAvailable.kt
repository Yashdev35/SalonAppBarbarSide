package com.example.sallonappbarbar.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//@Parcelize
//data class ServiceLevel(
//    val serviceLevelHeading: String,
//    val servicePrice: String,
//    val id: Int,
//):Parcelable

@Parcelize
data class aService(
//    var servicesLevel: List<ServiceLevel> = emptyList(),
    var isServiceSelected: Boolean = false,
    var price: String,
    val serviceTypeHeading: String,
    val serviceName: String,
    val id: Int,
    var time: String
):Parcelable
@Parcelize
data class ServiceType(
    val serviceTypeHeading: String,
    var aServices: List<aService>
):Parcelable

data class ServiceUploaded(
    val servicePrice: String,
    val serviceDuration: String,
)
