package com.example.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage

@Composable
fun CustomImage(size: Dp, url: String?, cornerRadius : Int){
    val musicNoteIcon = rememberVectorPainter(Icons.Outlined.MusicNote)

    AsyncImage(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(percent = cornerRadius))
            .background(MaterialTheme.colorScheme.secondary),
        model = url,
        contentDescription = "Image",
        placeholder = musicNoteIcon,
        fallback = musicNoteIcon,
        error = musicNoteIcon,
        contentScale = ContentScale.Crop,
    )
}