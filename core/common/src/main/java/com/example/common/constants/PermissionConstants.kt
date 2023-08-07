package com.example.common.constants

import android.Manifest
import android.os.Build

object PermissionConstants {
    val PERMISSIONS_STORAGE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}