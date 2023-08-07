package com.example.common

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(title: String?, showBackButton: Boolean, onBackClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier
            .background(color =  MaterialTheme.colorScheme.background),
        title = {
            if (title != null) {
                Text(text = title)
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }
        },
        actions = {
            // Добавление других кнопок или элементов настройки
        }
    )
}