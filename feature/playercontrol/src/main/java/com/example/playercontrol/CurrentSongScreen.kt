package com.example.playercontrol

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay5
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.common.CustomImage
import com.example.common.Toolbar
import com.example.common.utils.FormatTime
import com.example.common.viewmodels.MediaViewModel
import com.example.common.viewmodels.MediaViewModel.UIEvent
import com.example.model.Song

@Composable
fun CurrentSongScreen(
    navController: NavController,
    mediaViewModel: MediaViewModel,
) {
    val song: Song = mediaViewModel.currentSong
    val (progress, progressString) = Pair(mediaViewModel.progress, mediaViewModel.progressString)
    val durationString = FormatTime.formatDuration(mediaViewModel.duration)
    val onUiEvent: (UIEvent) -> Unit = { uiEvent: UIEvent -> mediaViewModel.onUIEvent(uiEvent) }

    val playImage = if (mediaViewModel.isPlaying) Icons.Filled.Pause
    else Icons.Filled.PlayArrow

    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.primary
                ),
            )
        )
    ) {
        Toolbar(
            title = null,
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )
        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomImage(200.dp, song.photoUri, 20)
                Spacer(modifier = Modifier.size(20.dp))
                MusicInfo(song = song, horizontalAlignment = Alignment.CenterHorizontally)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerBar(
                progress = progress,
                durationString = durationString,
                progressString = progressString,
                onUiEvent = onUiEvent
            )
            MusicControlExtended(onUiEvent = onUiEvent, playImage = playImage)
        }
    }
}

@Composable
fun MusicControlExtended(
    playImage: ImageVector,
    onUiEvent: (UIEvent) -> Unit
){
    val iconSize = 55.dp
    val smallIconSize = 30.dp

    Row(modifier = Modifier.padding(horizontal = 50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onUiEvent(UIEvent.Backward)}) {
            Icon(imageVector = Icons.Filled.Replay5,
                contentDescription = Icons.Filled.Replay5.name,
                tint = Color.White,
                modifier = Modifier
                    .size(smallIconSize)
            )
        }
        IconButton(
            modifier = Modifier
                .size(iconSize),
            onClick = { onUiEvent(UIEvent.PlayPrevious)}) {
            Icon(imageVector = Icons.Filled.SkipPrevious,
                contentDescription = Icons.Filled.SkipPrevious.name,
                tint = Color.White,
                modifier = Modifier
                    .size(iconSize),
            )
        }
        IconButton(
            modifier = Modifier
                .size(iconSize),
            onClick = { onUiEvent(UIEvent.PlayPause) }) {
            Icon(imageVector = playImage,
                contentDescription = Icons.Filled.SkipPrevious.name,
                tint = Color.White,
                modifier = Modifier
                    .size(iconSize)
            )
        }
        IconButton(
            modifier = Modifier
                .size(iconSize),
            onClick = { onUiEvent(UIEvent.PlayNext)}) {
            Icon(imageVector = Icons.Filled.SkipNext,
                contentDescription = Icons.Filled.SkipNext.name,
                tint = Color.White,
                modifier = Modifier
                    .size(iconSize)
            )
        }
        IconButton(onClick = { onUiEvent(UIEvent.Forward)}) {
            Icon(imageVector = Icons.Filled.FastForward,
                contentDescription = Icons.Filled.FastForward.name,
                tint = Color.White,
                modifier = Modifier
                    .size(smallIconSize)
            )
        }
    }
}