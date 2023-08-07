package com.example.playercontrol

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.common.viewmodels.MediaViewModel.UIEvent


@Composable
internal fun PlayerBar(
    progress: Float,
    durationString: String,
    progressString: String,
    onUiEvent: (UIEvent) -> Unit
) {
    val newProgressValue = remember { mutableStateOf(0f) }
    val useNewProgressValue = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Slider(
            value = if (useNewProgressValue.value) newProgressValue.value else progress,
            onValueChange = { newValue ->
                useNewProgressValue.value = true
                newProgressValue.value = newValue
                onUiEvent(UIEvent.UpdateProgress(newProgress = newValue))
            },
            onValueChangeFinished = {
                useNewProgressValue.value = false
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = progressString,
                //style = Typography.labelSmall,
                color = Color.White)
            Text(text = durationString,
                //style = Typography.labelSmall,
                color = Color.White)
        }
    }
}