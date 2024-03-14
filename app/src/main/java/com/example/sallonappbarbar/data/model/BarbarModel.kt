package com.example.sallonappbarbar.data.model

data class BarberModelResponse(
    val item : BarberModelItem,
    val key: String? = ""
){
    data class BarberModelItem(
        val name: String? = "",
        val shopName: String? = "",
        val phoneNumber: String? = "",
        val saloonType: String? = "",
        val imageUris: List<String?> = emptyList(),
        val shopAddress: String? = "",
    )
}


