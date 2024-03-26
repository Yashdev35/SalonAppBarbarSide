package com.example.sallonappbarbar.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Service(
    val serviceName: String,
    var isServiceSelected: Boolean = false,
    var price: String,
    val serviceTypeHeading: String,
    val id: Int,
):Parcelable

data class ServiceType(
    val serviceTypeHeading: String,
    val services: List<Service>
)
