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

@Parcelize
data class ServiceModel(
    val name: String? = "",
    val price: String = "0",
    val time:String = "00:00"
): Parcelable

@Parcelize
data class ServiceCat(
    val type: String? = "",
    val services: List<ServiceModel> = emptyList()
): Parcelable
@Parcelize
data class Service(
    val serviceName: String,
    var count: Int = 0,
    var price: String,
    val time :String,
    val id: String,
    val type:String
): Parcelable
