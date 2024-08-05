package com.example.sallonappbarbar.data.model

import com.example.sallonappbarbar.appUi.viewModel.OrderStatus

data class OrderModel(
    val imageUrl: String,
    val orderType: List<String>,
    val timeSlot: List<String>,
    val phoneNumber: String,
    val customerName: String,
    val paymentMethod: String? = "Cash",
    var orderStatus: OrderStatus = OrderStatus.PENDING,
    var isCancelRequested: Boolean = false,
    val orderId: String = "",
    val date: String = "",
)
data class ReviewModel(
    var rating: Double = 0.0,
    var reviewText: String = "",
    val orderId: String = "",
    val userDp: String = "",
    val userName: String = "",
)