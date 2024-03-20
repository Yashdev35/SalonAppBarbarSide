package com.example.sallonappbarbar.appUi.Screens.initiators

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.practicecoding.sallonapp.appui.components.GeneralButton

@Composable
fun MultiplePhotoPicker(
    selectedImagesUris: MutableState<List<Uri>>,
){
val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickMultipleVisualMedia(),
    onResult = { uris -> selectedImagesUris.value = uris }
)
    LazyColumn(
        modifier = Modifier.padding(16.dp).wrapContentSize(),
        ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                GeneralButton(
                    text = "Add Photos",
                    width = 200,
                    height = 50,
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
        items(selectedImagesUris.value) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun MultiplePhotoPickerPreview(){
    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val scrollState = rememberScrollState()
    MultiplePhotoPicker(selectedImagesUris = mutableStateOf(selectedImageUris))
}