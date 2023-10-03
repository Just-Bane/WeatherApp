package com.example.composeweatherapp.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object City: BottomBarScreen(
        route = "city",
        title = "City",
        icon = Icons.Default.Search
    )
    object Home: BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Location: BottomBarScreen(
        route = "location",
        title = "Location",
        icon = Icons.Default.Place
    )
}