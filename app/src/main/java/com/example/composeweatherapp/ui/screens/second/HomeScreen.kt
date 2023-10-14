package com.example.composeweatherapp.ui.screens.second

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.composeweatherapp.R
import com.example.composeweatherapp.ui.screens.main.BoxesSection
import com.example.composeweatherapp.ui.screens.main.GradientBackgroundBrush
import com.example.composeweatherapp.ui.screens.main.LocationSection
import com.example.composeweatherapp.ui.screens.main.TemperatureSection
import com.example.composeweatherapp.ui.screens.main.WeatherUIData
import com.example.composeweatherapp.ui.theme.gradientColorList

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    if (homeViewModel.isOnline.value) {
       homeViewModel.screenState.value = HomeViewModel.HomeScreenState.Default
    } else {
        homeViewModel.screenState.value = HomeViewModel.HomeScreenState.NoInternet
    }

    when(homeViewModel.screenState.value) {
        HomeViewModel.HomeScreenState.Default -> {
            HomeScreenUI(modifier = modifier)
        }
        HomeViewModel.HomeScreenState.NoInternet -> {
            navController.navigate("internet")
        }
    }
}


@Composable
fun HomeScreenUI(modifier: Modifier) {

    val homeViewModel: HomeViewModel = hiltViewModel()

    val locationWeather = homeViewModel.weather

    val context = LocalContext.current

    Surface(
        modifier.fillMaxSize()
    ) {
        Column(
            modifier.background(
                GradientBackgroundBrush(
                    isVerticalGradient = true,
                    colors = gradientColorList
                )
            )
        ) {
            LocationSection(
                city = locationWeather.value.name,
                time = homeViewModel.currentDate
            )
            TemperatureSection(
                temperature = context.getString(R.string.degrees, locationWeather.value.temp)
            )
            BoxesSection(
                weatherUIData = listOf(
                    WeatherUIData(
                        title = context.getString(R.string.wind),
                        iconId = R.drawable.wind,
                        weatherData = context.getString(R.string.speed, locationWeather.value.speed)
                    ),
                    WeatherUIData(
                        title = context.getString(R.string.humid),
                        iconId = R.drawable.humid,
                        weatherData = locationWeather.value.humidity + " %"
                    ),
                    WeatherUIData(
                        title = context.getString(R.string.clouds),
                        iconId = R.drawable.cloud,
                        weatherData = locationWeather.value.description
                    )
                )
            )
        }
    }
}


//@Composable
//fun SecondScreenExample(
//    modifier: Modifier
//) {
//
//    val homeViewModel: HomeViewModel = hiltViewModel()
//
//    val screenState = homeViewModel.screenState.value
//
//    Surface(
//        modifier.fillMaxSize()
//    ) {
//        Column(
//            Modifier.background(
//                GradientBackgroundBrush(
//                    isVerticalGradient = true,
//                    colors = gradientColorList
//                )
//            )
//        ) {
//            when (screenState) {
//                is HomeViewModel.SecondScreenState.CorrectPassword -> {
//                    SimpleText(
//                        text = screenState.text
//                    )
//                }
//
//                is HomeViewModel.SecondScreenState.Error -> {
//                    ErrorMessage(
//                        text = screenState.text,
//                        button = screenState.button
//                    ) {
//                        homeViewModel.proceedIntent(HomeViewModel.SecondScreenIntent.RetryIntent)
//                    }
//                }
//
//                is HomeViewModel.SecondScreenState.Logged -> {
//                    SimpleText(text = screenState.text)
//                }
//
//                HomeViewModel.SecondScreenState.NoInternet -> {
//                    ButtonAndText(
//                        text = "No Internet",
//                        button = "Retry"
//                    ) {
//                        homeViewModel.proceedIntent(HomeViewModel.SecondScreenIntent.CheckAndRetryIntent)
//                    }
//                }
//
//                is HomeViewModel.SecondScreenState.PrintPassword -> {
//                    ButtonAndText(
//                        text = screenState.text,
//                        button = screenState.button
//                    ) {
//                        homeViewModel.proceedIntent(HomeViewModel.SecondScreenIntent.LoginIntent("",""))
//                    }
//                }
//
//                is HomeViewModel.SecondScreenState.WrongPassword -> {
//                    ButtonAndText(
//                        text = screenState.text,
//                        button = screenState.button
//                    ) {
//                        homeViewModel.proceedIntent(HomeViewModel.SecondScreenIntent.LoginIntent("",""))
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ButtonAndText(text: String, button: String, onClickEvent: () -> Unit) {
//    Text(text)
//    Button(onClick = {
//        onClickEvent()
//    }) {
//        Text(button)
//    }
//}
//
//@Composable
//fun SimpleText(text: String) {
//    Text(text)
//}
//
//@Composable
//fun ErrorMessage(text: Exception, button: String, onClickEvent: () -> Unit) {
//    Text(
//        modifier = Modifier.background(Color.Red),
//        text = text.message!!
//    )
//    Button(onClick = {
//        onClickEvent()
//    }) {
//        Text(button)
//    }
//}
