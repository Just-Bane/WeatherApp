package com.example.composeweatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composeweatherapp.ui.screens.first.CityScreen
import com.example.composeweatherapp.ui.screens.first.CityViewModel
import com.example.composeweatherapp.ui.screens.internet.InternetScreen
import com.example.composeweatherapp.ui.screens.second.HomeScreen
import com.example.composeweatherapp.ui.screens.third.LocationScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = BottomBarScreen.City.route) {
            CityScreen(modifier = Modifier, navController = navController)
        }
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(modifier = Modifier, navController = navController)
        }
        composable(route = BottomBarScreen.Location.route) {
            LocationScreen(modifier = Modifier, navController = navController)
        }
        composable(route = "internet") {
            InternetScreen(modifier = Modifier, navController = navController)
        }
    }
}