package com.example.songs.navigation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.common.viewmodels.MediaViewModel
import com.example.data.viewmodels.StorageViewModel

@Composable
fun SongsScreen(
    navController: NavController,
    mediaViewModel: MediaViewModel
){
    val storageViewModel: StorageViewModel = hiltViewModel()

    val currentSongPosition: Int = mediaViewModel.songIndex

    Box(modifier = Modifier.fillMaxSize()) {
        val songListState = storageViewModel.songs.observeAsState(emptyList())
        SongsList(
            onUiEvent = mediaViewModel::onUIEvent,
            songs = songListState.value,
            currentSongPosition = currentSongPosition,
        )
    }
}