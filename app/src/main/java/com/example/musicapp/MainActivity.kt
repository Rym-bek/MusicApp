package com.example.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.common.viewmodels.MediaViewModel
import com.example.musicapp.ui.MusicApp
import com.example.musicapp.ui.theme.MusicAppTheme
import com.example.musicapp.util.PermissionAction
import com.example.musicapp.util.PermissionDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mediaViewModel: MediaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isPermissionGranted by rememberSaveable{ mutableStateOf(false) }
            MusicAppTheme {
                PermissionDialog(
                    context = LocalContext.current,
                    permissionAction = { permissionAction ->
                        isPermissionGranted = when (permissionAction) {
                            PermissionAction.PermissionGranted -> true
                            PermissionAction.PermissionDenied -> false
                        }
                    },
                    modifier = Modifier,
                )

                if (isPermissionGranted) {
                    MusicApp(mediaViewModel = mediaViewModel)
                }
            }
        }
    }
}
