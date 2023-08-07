package com.example.musicapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.common.constants.PermissionConstants

@Composable
fun PermissionDialog(
    permissionAction: (PermissionAction) -> Unit,
    modifier: Modifier,
    context: Context,
) {
    val activity = context as? Activity ?: return
    val permission = PermissionConstants.PERMISSIONS_STORAGE

    var isPermissionDenied by rememberSaveable { mutableStateOf(false) }

    if (isPermissionDenied) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            AlertDialog(
                onDismissRequest = {  },
                backgroundColor = MaterialTheme.colorScheme.primary,
                buttons = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Divider()
                        Text(
                            text = "OK",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    activity.openAppSettings()
                                    isPermissionDenied = false
                                }
                                .padding(16.dp)
                        )
                    }
                },
                title = {
                    Text(
                        text = "Permission required",
                        color = Color.White)
                },
                text = {
                    Text(
                        text = "The app need to be granted read permission in order to fetch songs from device",
                        color = Color.White
                    )
                },
                modifier = Modifier
            )
        }
    }

    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
        permissionAction(PermissionAction.PermissionGranted)
    } else {
        val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                permissionAction(PermissionAction.PermissionGranted)
            } else {
                isPermissionDenied = true
                permissionAction(PermissionAction.PermissionDenied)
            }
        }
        SideEffect {
            permissionLauncher.launch(permission)
        }
    }
}


fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

sealed interface PermissionAction {
    data object PermissionGranted: PermissionAction
    data object PermissionDenied: PermissionAction
}