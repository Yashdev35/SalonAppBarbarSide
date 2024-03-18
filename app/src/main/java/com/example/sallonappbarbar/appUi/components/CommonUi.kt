package com.practicecoding.sallonapp.appui.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.ui.theme.Purple40
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor

@Composable
fun CommonDialog() {
    Dialog(onDismissRequest = { }) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(60.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 12.dp),
                    color = Color(sallonColor.toArgb()),
                    trackColor = Color(purple_200.toArgb()),
                )
                Text(
                    text = "Loading",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .wrapContentSize()
                        .padding(30.dp),

                    color = Purple40
                )
            }

        }    }

}

@Composable
fun LaunchPhotoPicker(singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {

    singlePhotoPickerLauncher.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )

}

@Composable
fun PasswordFields() {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        // Password Field
        androidx.compose.material.OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { androidx.compose.material.Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    androidx.compose.material.Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.Lock else Icons.Filled.Lock,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        // Confirm Password Field
        androidx.compose.material.OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { androidx.compose.material.Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    androidx.compose.material.Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.Lock else Icons.Filled.Lock,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
    }
}

@Composable
fun GoogleAndFacebook() {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_google),
            contentDescription = "Google",
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    Toast
                        .makeText(context, "Google", Toast.LENGTH_SHORT)
                        .show()
                }
        )
        Spacer(modifier = Modifier.size(50.dp))
        Image(
            painter = painterResource(id = R.drawable.icon_facebook),
            contentDescription = "Facebook",
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    Toast
                        .makeText(context, "Facebook", Toast.LENGTH_SHORT)
                        .show()
                }
        )
    }
}
//@Preview(showBackground = true)
//@Composable
//fun AdvancedSignUpScreenPreview() {
//    CommonDialog()
//}
