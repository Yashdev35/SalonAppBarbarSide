package com.example.sallonappbarbar.appUi.viewModel

import android.app.Application
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.OrderModel
import com.example.sallonappbarbar.data.model.ReviewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
): ViewModel() {
    private val _orderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val orderList: StateFlow<MutableList<OrderModel>> = _orderList.asStateFlow()
    private val _acceptedOrderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val acceptedOrderList: StateFlow<MutableList<OrderModel>> = _acceptedOrderList.asStateFlow()
    private val _pendingOrderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val pendingOrderList: StateFlow<MutableList<OrderModel>> = _pendingOrderList.asStateFlow()
    private val _completedOrderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val completedOrderList: StateFlow<MutableList<OrderModel>> = _completedOrderList.asStateFlow()
    private val _cancelledOrderList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val cancelledOrderList: StateFlow<MutableList<OrderModel>> = _cancelledOrderList.asStateFlow()

    private var _todayPendingOrderNo = MutableStateFlow(0)
    var todayPendingOrderNo :StateFlow<Int> = _todayPendingOrderNo.asStateFlow()
    private var _todayAcceptedOrderNo = MutableStateFlow(0)
    var todayAcceptedOrderNo :StateFlow<Int> = _todayAcceptedOrderNo.asStateFlow()

    private val _reviewList = MutableStateFlow(emptyList<OrderModel>().toMutableList())
    val reviewList: StateFlow<MutableList<OrderModel>> = _reviewList.asStateFlow()



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
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            repo.getOrder().collect { orders ->
                _orderList.emit(orders.toMutableList())
                _pendingOrderList.update { it.toMutableList().apply { clear() } }
                _acceptedOrderList.update { it.toMutableList().apply { clear() } }
                _completedOrderList.update { it.toMutableList().apply { clear() } }
                _cancelledOrderList.update { it.toMutableList().apply { clear() } }
                _reviewList.update { it.toMutableList().apply { clear() } }
                _todayPendingOrderNo.update { 0 }
                _todayAcceptedOrderNo.update { 0 }
                orders.forEach { order ->
                    when (order.orderStatus) {
                        OrderStatus.PENDING -> {
                            if (order.date >= today) {
                                _pendingOrderList.update {
                                    it.toMutableList().apply {
                                        add(order)
                                    }
                                }
                                if(order.date==today){
                                    _todayPendingOrderNo.update { it.inc() }
                                }
                            } else {
                                order.orderStatus = OrderStatus.CANCELLED
                                _cancelledOrderList.update {
                                    it.toMutableList().apply {
                                        add(order)
                                    }
                                }
                                updateOrderStatus(order, OrderStatus.CANCELLED.status)
                            }
                        }

                        OrderStatus.ACCEPTED -> {
                            if (order.date >= today) {
                                _acceptedOrderList.update {
                                    it.toMutableList().apply {
                                        add(order)
                                    }
                                }
                                if(order.date==today){
                                    _todayAcceptedOrderNo.update { it.inc() }
                                }
                            } else {
                                order.orderStatus = OrderStatus.CANCELLED
                                _cancelledOrderList.update {
                                    it.toMutableList().apply {
                                        add(order)
                                    }
                                }
                                updateOrderStatus(order, OrderStatus.CANCELLED.status)
                            }
                        }

                        OrderStatus.COMPLETED -> {
                            _completedOrderList.update {
                                it.toMutableList().apply {
                                    add(order)
                                }
                            }
                            if(order.review.reviewTime.isNotEmpty()){
                                _reviewList.update { it.toMutableList().apply { add(order) } }
                            }
                        }

                        OrderStatus.CANCELLED -> {
                            _cancelledOrderList.update {
                                it.toMutableList().apply {
                                    add(order)
                                }
                            }
                        }
                    }
                }
            }

        }

    }
    suspend fun updateOrderStatus(order: OrderModel, status: String) {
        viewModelScope.launch {
            repo.updateOrderStatus(order, status).collect {
                when (it) {
                    is Resource.Success -> {
                    }

                    is Resource.Failure -> {
                    }

                    else -> {}
                }
            }

        }

    }
}

sealed class OrderEvent {
    data object GetOrderList : OrderEvent()
    data object UpdateOrderStatus : OrderEvent()
    data object GetPendingOrderList : OrderEvent()
}

@Parcelize
enum class OrderStatus(val status: String) : Parcelable {
    PENDING("pending"),
    ACCEPTED("accepted"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
}