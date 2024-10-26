package com.example.sallonappbarbar.appUi.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.sallonappbarbar.data.model.OrderModel

fun sendSms(order:OrderModel,context: Context,message:String){
    try {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(
                order.userPhoneNumber,
                null,
                message,
                null,
                null
            )
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Message sent error", Toast.LENGTH_LONG).show()

    }
}