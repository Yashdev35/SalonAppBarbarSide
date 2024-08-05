package com.example.sallonappbarbar.appUi.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.OrderModel
import com.example.sallonappbarbar.data.model.ReviewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
): ViewModel() {
    private val _orderList = mutableStateOf<List<OrderModel>>(emptyList())
    val orderList: State<List<OrderModel>> = _orderList

    private val _reviewList = mutableStateOf<List<ReviewModel>>(emptyList())
    val reviewList: State<List<ReviewModel>> = _reviewList

    private val _averageRating = mutableStateOf(0.0)
    val averageRating: State<Double> = _averageRating

    private fun getAverageRating() {
        var totalRating = 0.0
        var totalReview = 0
        for (review in _reviewList.value) {
            totalRating += review.rating
            totalReview++
        }
        _averageRating.value = totalRating / totalReview
    }

    private val _acceptedOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val acceptedOrderList: State<List<OrderModel>> = _acceptedOrderList

    private val _pendingOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val pendingOrderList: State<List<OrderModel>> = _pendingOrderList
    var todayOrderNo = mutableStateOf(0)
    var isLoading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            getOrders()
        }
    }
    suspend fun onEvent(event: OrderEvent) {
        when(event) {
            is OrderEvent.GetOrderList -> getOrders()
            is OrderEvent.GetPendingOrderList -> { }
            is OrderEvent.UpdateOrderStatus -> {}
        }
    }

    private suspend fun getOrders() {
        isLoading.value = true
        repo.getOrders { orders ->
            _orderList.value = orders
            _acceptedOrderList.value = orders.filter { it.orderStatus == OrderStatus.ACCEPTED }
            _pendingOrderList.value = orders.filter { it.orderStatus == OrderStatus.PENDING }
            todayOrderNo.value = 0
            for (pendingOrder in _pendingOrderList.value) {
                if (pendingOrder.date == LocalDate.now().toString()) {
                    todayOrderNo.value++
                }
            }
            isLoading.value = false
            viewModelScope.launch{
                getReviews()
            }
        }
    }
    private suspend fun getReviews() {
        repo.getReviews{reviews ->
            _reviewList.value = reviews
            Log.d("reviewViewModel", "getReviews: ${_reviewList.value}")
            getAverageRating()
        }
    }
    suspend fun updateOrderStatus(orderId: String, status: String) {
        repo.updateOrderStatus(orderId, status).collect(){
            when(it){
                is Resource.Success -> {
                    Log.d("OrderViewModel", "updateOrderStatus: Success")
                    todayOrderNo.value = 0
                    Log.d("OrderViewModel", "updateOrderStatus: ${LocalDate.now()}")
                    for (pendingOrder in _pendingOrderList.value) {
                        if (pendingOrder.date == LocalDate.now().toString()) {
                            todayOrderNo.value++
                            Log.d("OrderViewModel", "updateOrderStatus: ${todayOrderNo.value}")
                        }
                    }
                }
                is Resource.Failure -> {
                    Log.d("OrderViewModel", "updateOrderStatus: Error")
                }
                else -> {}
            }
        }
    }
}

sealed class OrderEvent {
    object GetOrderList : OrderEvent()
    object UpdateOrderStatus : OrderEvent()
    object GetPendingOrderList : OrderEvent()
}

enum class OrderStatus(val status: String) {
    PENDING("pending"),
    ACCEPTED("accepted"),
    DECLINED("declined"),
    COMPLETED("completed"),
}