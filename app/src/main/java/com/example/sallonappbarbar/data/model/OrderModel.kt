package com.example.sallonappbarbar.data.model

import android.os.Parcelable
import com.example.sallonappbarbar.appUi.viewModel.OrderStatus
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class OrderModel(
    val barberuid: String="",
    val useruid: String="",
    val userImageUrl: String,
    val listOfService:  List<Service>,
    val timeSlot: List<TimeSlot>,
    val userPhoneNumber: String,
    val userName: String,
    val paymentMethod: String? = "Cash",
    var orderStatus: OrderStatus = OrderStatus.PENDING,
    var isCancelRequested: Boolean = false,
    val orderId: String = "",
    val date: String = "",
    var review:ReviewModel = ReviewModel(),
    val genderCounter:List<Int> =listOf()
) : Parcelable

@Serializable
@Parcelize
data class ReviewModel(
    var rating: Double = 0.0,
    var reviewText: String = "",
    val orderId: String = "",
    val userDp: String = "",
    val userName: String = "",
    val reviewTime:String=""
):Parcelable

@Serializable
@Parcelize
data class BookedModel(
    var barberuid: String = "",
    var genderCounter: List<Int> = listOf(0, 0, 0),
    var listOfService: List<Service> = emptyList(),
    var selectedSlots: List<TimeSlot> = emptyList(),
    var selectedDate: String = "",
    var status: String = "pending",
    var useruid: String = "",
    var dateandtime:String="",
    var paymentMethod: String = "Cash",
    var review:ReviewModel = ReviewModel()
) : Parcelable