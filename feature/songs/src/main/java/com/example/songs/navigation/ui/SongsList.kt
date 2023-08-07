package com.example.songs.navigation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.common.CustomImage
import com.example.common.viewmodels.MediaViewModel.UIEvent
import com.example.model.Song

@Composable
fun SongsList(
    onUiEvent: (UIEvent) -> Unit,
    songs: List<Song>,
    currentSongPosition: Int,
) {

    LazyColumn(modifier = Modifier
        .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        itemsIndexed(songs) { index, song ->
            MessageRow(onUiEvent = onUiEvent, song, index, currentSongPosition)
            Divider(
                modifier = Modifier.padding(start = 80.dp, end = 20.dp),
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun MessageRow(onUiEvent: (UIEvent) -> Unit,
               song: Song,
               index: Int,
               currentSongPosition: Int?
) {

    val isCurrentSong = currentSongPosition == index
    val mainTextColor= if (isCurrentSong) MaterialTheme.colorScheme.primary else MaterialTheme.typography.bodyLarge.color
    val secondTextColor = if (isCurrentSong) MaterialTheme.colorScheme.primary else MaterialTheme.typography.labelSmall.color

    Row(modifier = Modifier
        .clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onUiEvent(UIEvent.PlaySong(index))
        }
        .fillMaxWidth()
        .padding(top = 15.dp, bottom = 15.dp,start = 15.dp)
        .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,) {

        CustomImage(
            size = 50.dp,
            url  = song.photoUri,
            cornerRadius = 20
        )

        Column(modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)){
            song.title?.let {
                Text(maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = mainTextColor
                )
            }
            song.artist?.let {
                Text(maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = secondTextColor
                )
            }
        }
    }
}