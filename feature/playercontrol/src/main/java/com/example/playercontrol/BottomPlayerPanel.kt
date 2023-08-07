package com.example.playercontrol

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.common.CustomImage
import com.example.common.viewmodels.MediaViewModel.UIEvent
import com.example.model.Song
import com.example.playercontrol.navigation.navigateToCurrentSong


@Composable
fun BottomPlayerPanel(
    playImage: () -> ImageVector,
    onUiEvent: (UIEvent) -> Unit,
    song: Song?,
    navController: NavController,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(percent = 50))
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                navController.navigateToCurrentSong()
            },
        verticalAlignment = Alignment.CenterVertically,
    ){

        CustomImage(40.dp, song?.photoUri,50)

        MusicInfo(song,
            Modifier
                .weight(1f)
                .padding(start = 10.dp),Color.White)

        MusicControl(onUiEvent = onUiEvent,playImage = playImage)
    }
}

@Composable
fun MusicControl(playImage: () -> ImageVector,
                 onUiEvent: (UIEvent) -> Unit
){
    Row(horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onUiEvent(UIEvent.PlayPrevious)}) {
            Icon(imageVector = Icons.Filled.SkipPrevious,
                contentDescription = Icons.Filled.SkipPrevious.name,
                tint = Color.White
            )
        }
        IconButton(onClick = { onUiEvent(UIEvent.PlayPause) }) {
            Icon(imageVector = playImage(),
                contentDescription = Icons.Filled.SkipPrevious.name,
                tint = Color.White
            )
        }
        IconButton(onClick = { onUiEvent(UIEvent.PlayNext)}) {
            Icon(imageVector = Icons.Filled.SkipNext,
                contentDescription = Icons.Filled.SkipNext.name,
                tint = Color.White
            )
        }
    }
}

@Composable
fun MusicInfo(song: Song?, modifier: Modifier = Modifier, color: Color = Color.Unspecified,horizontalAlignment: Alignment.Horizontal = Alignment.Start)
{
    Column(modifier = modifier,
    horizontalAlignment = horizontalAlignment,
    ) {
        song?.title?.let {
            Text(maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = color)
        }
        song?.artist?.let {
            Text(maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}