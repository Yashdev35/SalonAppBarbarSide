package com.example.sallonappbarbar.appUi.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.model.OrderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
): ViewModel() {
    private val _orderList = mutableStateOf<List<OrderModel>>(emptyList())
    val orderList: State<List<OrderModel>> = _orderList

    private val _acceptedOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val acceptedOrderList: State<List<OrderModel>> = _acceptedOrderList

    private val _pendingOrderList = mutableStateOf<List<OrderModel>>(emptyList())
    val pendingOrderList: State<List<OrderModel>> = _pendingOrderList

    var isLoading = mutableStateOf(false)

    init {
        viewModelScope.launch { getOrders() }
    }

    private suspend fun getOrders() {
        isLoading.value = true
        repo.getOrders { orders ->
            _orderList.value = orders
            Log.d("OrderViewModel", "getOrders: $orders")
            _acceptedOrderList.value = orders.filter { it.orderStatus == OrderStatus.ACCEPTED }
            _pendingOrderList.value = orders.filter { it.orderStatus == OrderStatus.PENDING }
            Log.d("aOrderViewModel", "getOrders: $acceptedOrderList")
            isLoading.value = false
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