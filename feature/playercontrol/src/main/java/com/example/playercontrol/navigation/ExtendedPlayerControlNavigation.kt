package com.example.playercontrol.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.viewmodels.MediaViewModel
import com.example.playercontrol.CurrentSongScreen


const val currentSongScreenRoute = "currentSong"

fun NavController.navigateToCurrentSong() {
    navigate(currentSongScreenRoute)
}

fun NavGraphBuilder.currentSongScreen(navController: NavController, mediaViewModel: MediaViewModel) {
    composable(currentSongScreenRoute) {
        CurrentSongScreen(navController = navController, mediaViewModel = mediaViewModel)
    }
}