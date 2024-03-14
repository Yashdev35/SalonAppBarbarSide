package com.example.sallonappbarbar.appUi.viewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModelResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarberDataViewModel (
    private val repo:FirestoreRepository
) :ViewModel(){
private val _barberDataState :MutableState<BarberDataState> = mutableStateOf(BarberDataState())
val barberDataState : State<BarberDataState> = _barberDataState

    fun insertData(
        item:BarberModelResponse.BarberModelItem
    ) = viewModelScope.launch {
        _barberDataState.value = _barberDataState.value.copy(isLoading = true)
        repo.insert(item).collect {
            _barberDataState.value = _barberDataState.value.copy(isLoading = false)
            getItems()
        }
    }

    private val _updateBarberData: MutableState<BarberModelResponse> =
        mutableStateOf(BarberModelResponse(item = BarberModelResponse.BarberModelItem()))

    val updateBarberData: State<BarberModelResponse> = _updateBarberData

    fun updateData(data: BarberModelResponse) = viewModelScope.launch {
        _updateBarberData.value = data
    }

    init {
        getItems()
    }

        fun getItems() = viewModelScope.launch {
            _barberDataState.value = _barberDataState.value.copy(isLoading = true)
            repo.getItems().collect {
                _barberDataState.value = _barberDataState.value.copy(isLoading = false)
                when(it){
                    is Resource.Success -> {
                        _barberDataState.value = BarberDataState(data = it.result)
                    }
                    is Resource.Failure -> {
                        _barberDataState.value = BarberDataState(error = it.exception.toString())
                    }
                    Resource.Loading->{
                        _barberDataState.value = BarberDataState(isLoading = true)
                    }
                }
            }
        }

        fun deleteData(key:String) = viewModelScope.launch {
            _barberDataState.value = _barberDataState.value.copy(isLoading = true)
            repo.delete(key).collect {
                _barberDataState.value = _barberDataState.value.copy(isLoading = false)
            }
        }
        fun update(item:BarberModelResponse) = repo.update(item)


}

data class BarberDataState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<BarberModelResponse> = emptyList()
)