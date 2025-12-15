// utils/ImagePicker.kt
package com.example.drivenext.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

class ImagePicker(private val activity: Activity) {

    companion object {
        const val REQUEST_GALLERY = 1001
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, REQUEST_GALLERY)
    }
}