package com.example.musicapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.common.viewmodels.MediaViewModel
import com.example.playercontrol.navigation.currentSongScreen
import com.example.search.navigation.searchScreen
import com.example.songs.navigation.songsNavigationRoute
import com.example.songs.navigation.songsScreen

@Composable
fun MusicNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    mediaViewModel: MediaViewModel
) {
    NavHost(
        navController = navController,
        startDestination = songsNavigationRoute,
        modifier = modifier
    ) {

        songsScreen(
            navController = navController, mediaViewModel = mediaViewModel)

        searchScreen(
            navController = navController)

        currentSongScreen(
            navController = navController,
            mediaViewModel = mediaViewModel)
    }
}

