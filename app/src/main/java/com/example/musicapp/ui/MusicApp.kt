package com.example.musicapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.common.viewmodels.MediaViewModel
import com.example.model.Song
import com.example.musicapp.navigation.BottomBarDestination
import com.example.musicapp.navigation.MusicNavHost
import com.example.playercontrol.BottomPlayerPanel
import com.example.playercontrol.navigation.currentSongScreenRoute
import com.example.search.navigation.searchNavigationRoute
import com.example.songs.navigation.songsNavigationRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicApp(
    modifier: Modifier = Modifier,
    mediaViewModel: MediaViewModel
) {
    val navController = rememberNavController()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    var bottomPlayerPanelCollapsed by remember { mutableStateOf(true) }
    val destinations = listOf(BottomBarDestination.Songs, BottomBarDestination.Search)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentSong: Song = mediaViewModel.currentSong

    bottomPlayerPanelCollapsed = currentDestination?.route != currentSongScreenRoute

    val bottomBarRoutes = listOf(songsNavigationRoute, searchNavigationRoute)
    val showBottomBar = currentDestination?.route in bottomBarRoutes

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                showBottomBar && bottomPlayerPanelCollapsed,
                enter = slideInVertically{ it },
                exit = slideOutVertically{ it }
            ) {
                MusicBottomBar(
                    destinations = destinations,
                    currentDestination = currentDestination,
                    onNavigateToDestination = {
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        })
    { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            MusicNavHost(
                mediaViewModel = mediaViewModel,
                navController = navController,
                modifier = Modifier // Занимает всю доступную вертикальную область, кроме BottomPlayerPanel
            )
            Column(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.Bottom
            ) {
                AnimatedVisibility(
                    visible = bottomPlayerPanelCollapsed,
                    enter = slideInVertically{ it },
                    exit = slideOutVertically{ it }
                ) {
                    BottomPlayerPanel(
                        playImage = {
                            if (mediaViewModel.isPlaying) Icons.Filled.Pause
                            else Icons.Filled.PlayArrow
                        },
                        onUiEvent = mediaViewModel::onUIEvent,
                        song = currentSong,
                        navController = navController,
                    )
                }
            }
        }
    }
}