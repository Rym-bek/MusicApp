package com.example.musicapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.search.navigation.searchNavigationRoute
import com.example.songs.navigation.songsNavigationRoute



sealed class BottomBarDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
    val route: String,
) {


    data object Songs: BottomBarDestination(
        selectedIcon = Icons.Filled.LibraryMusic,
        unselectedIcon = Icons.Outlined.LibraryMusic,
        iconText = "Песни",
        route = songsNavigationRoute
    )

    data object Search: BottomBarDestination(
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        iconText = "Поиск",
        route = searchNavigationRoute
    )
}