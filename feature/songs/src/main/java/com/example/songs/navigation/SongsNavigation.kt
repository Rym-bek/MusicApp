package com.example.songs.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.viewmodels.MediaViewModel
import com.example.songs.navigation.ui.SongsScreen


const val songsNavigationRoute = "songs"

fun NavGraphBuilder.songsScreen(navController: NavController, mediaViewModel: MediaViewModel) {
    composable(songsNavigationRoute) {
        SongsScreen(navController = navController, mediaViewModel = mediaViewModel)
    }
}