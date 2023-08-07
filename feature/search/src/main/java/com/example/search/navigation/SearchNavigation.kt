package com.example.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.search.SearchScreen


const val searchNavigationRoute = "search"

fun NavGraphBuilder.searchScreen(navController: NavController) {
    composable(searchNavigationRoute) {
        SearchScreen(navController = navController)
    }
}