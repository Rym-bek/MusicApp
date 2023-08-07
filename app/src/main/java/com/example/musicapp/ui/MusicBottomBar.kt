package com.example.musicapp.ui

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.musicapp.navigation.BottomBarDestination

@Composable
fun MusicBottomBar(
    destinations: List<BottomBarDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (BottomBarDestination) -> Unit
) {
    BottomNavigation(
        modifier = Modifier,
        backgroundColor = (MaterialTheme.colorScheme.primary),
        contentColor = (MaterialTheme.colorScheme.primary)
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == destination.route
            } ?: false
            BottomNavigationItem(
                icon = {
                    if (selected) {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = destination.selectedIcon.name,
                            tint = Color.White,
                        )
                    } else {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = destination.unselectedIcon.name,
                            tint = Color.White,
                        )
                    }
                },
                label = {
                    Text(
                        text = destination.iconText,
                        color = Color.White,
                    ) },
                selected = selected,
                onClick = {
                    onNavigateToDestination(destination)
                }
            )
        }
    }
}

