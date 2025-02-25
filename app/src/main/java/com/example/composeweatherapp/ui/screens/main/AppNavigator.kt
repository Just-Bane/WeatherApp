package com.example.composeweatherapp.ui.screens.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composeweatherapp.R
import com.example.composeweatherapp.core.CITY_SCREEN
import com.example.composeweatherapp.core.LOCATION_SCREEN
import com.example.composeweatherapp.ui.nav.BottomBarScreen
import com.example.composeweatherapp.ui.nav.NavigationScreens
import com.example.composeweatherapp.ui.screens.city.CityScreen
import com.example.composeweatherapp.ui.screens.city.CityViewModel
import com.example.composeweatherapp.ui.screens.internet.InternetScreen
import com.example.composeweatherapp.ui.screens.home.HomeScreen
import com.example.composeweatherapp.ui.screens.location.LocationScreen
import com.example.composeweatherapp.ui.screens.location.LocationViewModel
import com.example.composeweatherapp.ui.theme.bottomBarColor
import com.example.composeweatherapp.ui.theme.boxesColor

@SuppressLint("SimpleDateFormat", "StringFormatInvalid", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigator(
    modifier: Modifier
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(route = NavigationScreens.City.route) {
            CityScreen(modifier = Modifier, navController = navController)
        }
        composable(route = NavigationScreens.Home.route) {
            HomeScreen(modifier = Modifier, navController = navController)
        }
        composable(route = NavigationScreens.Location.route) {
            LocationScreen(modifier = Modifier, navController = navController)
        }
        composable(route = NavigationScreens.Internet.route) {
            InternetScreen(modifier = Modifier, navController = navController)
        }
    }

}


@Composable
fun LocationSection(
    city: String,
    time: String,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.padding(
        horizontal = 18.dp,
        vertical = 8.dp
    )
) {
    val fontFamily = FontFamily(
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_bold, FontWeight.Bold)
    )
    Box(Modifier.fillMaxWidth()) {
        Column(modifier) {
            Text(
                text = city,
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF313341)
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = time,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF9A938C)
                )
            )
        }
    }
}

@Composable
fun TemperatureSection(temperature: String) {
    val fontFamily = FontFamily(
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_bold, FontWeight.Bold)
    )
    Box(
        Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier
                .padding(
                    horizontal = 30.dp, vertical = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.weather_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(width = 200.dp, height = 200.dp)
            )
            Spacer(Modifier.padding(5.dp))
            Text(
                text = temperature,
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF313341),
                )
            )

        }
    }
}

@Composable
fun BoxesSection(weatherUIData: List<WeatherUIData>) {
    Box() {
        LazyColumn(Modifier.padding(horizontal = 16.dp)) {
            items(weatherUIData.size) {
                BoxWithWeather(dataWeather = weatherUIData[it])
            }
        }
    }
}

@Composable
fun EnableDialog(screen: String) {

    val cityViewModel: CityViewModel = hiltViewModel()
    val locationViewModel: LocationViewModel = hiltViewModel()

    val openDialog = rememberSaveable {
        mutableStateOf(true)
    }
    openDialog.value = true
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                when (screen) {
                    CITY_SCREEN -> cityViewModel.changeToDefaultState()
                    LOCATION_SCREEN -> locationViewModel.changeToDefaultState()
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        when (screen) {
                            CITY_SCREEN -> cityViewModel.changeToDefaultState()
                            LOCATION_SCREEN -> locationViewModel.changeToDefaultState()
                        }
                    }
                ) {
                    Text("Ok")
                }
            },
            text = {
                Row(modifier = Modifier) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "This city does not exist"
                    )
                }
            }
        )
    }
}


@Composable
fun GradientBackgroundBrush(
    isVerticalGradient: Boolean,
    colors: List<Color>
): Brush {
    val endOffset = if (isVerticalGradient) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }
    return Brush.linearGradient(colors = colors, start = Offset.Zero, end = endOffset)
}

@Composable
fun BoxWithWeather(dataWeather: WeatherUIData) {
    val fontFamily = FontFamily(
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_bold, FontWeight.Bold)
    )
    Box(
        Modifier
            .fillMaxWidth()
            .padding()
            .background(color = boxesColor, shape = RoundedCornerShape(16.dp))
    ) {
        Row(
            Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .background(
                        color = Color(0xE5FFFFFF),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color(0x26C01B3C),
                        ambientColor = Color(0x26C01B3C)
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = dataWeather.iconId),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = dataWeather.title,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = dataWeather.weatherData,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavSection(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.City,
        BottomBarScreen.Home,
        BottomBarScreen.Location
    )
    BottomNavigation(
        backgroundColor = bottomBarColor
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}


@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: String?,
    navController: NavHostController
) {
    val fontFamily = FontFamily(
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_bold, FontWeight.Bold)
    )
    BottomNavigationItem(
        icon = {
            Icon(imageVector = screen.icon, contentDescription = "Navigation Icon")
        },
        label = {
            Text(
                text = screen.title,
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            )
        },
        selectedContentColor = Color.Black,
        unselectedContentColor = Color.Black.copy(0.4f),
        alwaysShowLabel = false,
        selected = currentDestination == screen.route,
        onClick = {
            navController.navigate(screen.route) {
                navController.graph.startDestinationRoute?.let {
                    popUpTo(it) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@Composable
fun ReconnectionSection() {
    val fontFamily = FontFamily(
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_bold, FontWeight.Bold)
    )

    Text(
        text = "Reconnecting",
        style = TextStyle(
            fontSize = 10.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Light,
            color = Color(0xFF9A938C)
        )
    )
}


