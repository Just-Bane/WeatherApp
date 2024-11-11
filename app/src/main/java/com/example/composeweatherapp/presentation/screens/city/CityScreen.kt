package com.example.composeweatherapp.presentation.screens.city

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.composeweatherapp.R
import com.example.composeweatherapp.common.core.CITY_SCREEN
import com.example.composeweatherapp.presentation.nav.NavigationScreens
import com.example.composeweatherapp.presentation.screens.home.HomeScreenEvents
import com.example.composeweatherapp.presentation.screens.main.BottomNavSection
import com.example.composeweatherapp.presentation.screens.main.BoxesSection
import com.example.composeweatherapp.presentation.screens.main.EnableDialog
import com.example.composeweatherapp.presentation.screens.main.GradientBackgroundBrush
import com.example.composeweatherapp.presentation.screens.main.LocationSection
import com.example.composeweatherapp.presentation.screens.main.ReconnectionSection
import com.example.composeweatherapp.presentation.screens.main.TemperatureSection
import com.example.composeweatherapp.presentation.screens.main.WeatherUIData
import com.example.composeweatherapp.presentation.theme.ComposeWeatherAppTheme
import com.example.composeweatherapp.presentation.theme.gradientColorList

@Composable
fun CityScreen(modifier: Modifier, navController: NavHostController) {
    val cityViewModel: CityViewModel = hiltViewModel()

    var state = cityViewModel.viewState.collectAsState()
    val event = cityViewModel.event.collectAsState()

    when (state.value) {
        is CityScreenState.WriteTheCity -> {
            CityScreenUI(modifier = Modifier, navController = navController)
        }
        is CityScreenState.CorrectCityWritten -> {
            CityScreenUI(modifier = Modifier, navController = navController)
            cityViewModel.changeToDefaultState()
        }
        is CityScreenState.NoInternet -> {
            CityScreenCheckingUI(modifier = Modifier, navController = navController)
        }
        is CityScreenState.WrongCityWritten -> {
            CityScreenUI(modifier = Modifier, navController = navController)
            EnableDialog(CITY_SCREEN)
        }
    }

    when (event.value) {
        CityScreenEvents.NavigateToInternetScreen -> {
            navController.navigate(NavigationScreens.Internet.route)
        }
        null -> {}
    }
}


@Composable
fun CityScreenUI(
    modifier: Modifier,
    navController: NavHostController
) {
    val cityViewModel: CityViewModel = hiltViewModel()

    val locationWeather = cityViewModel.weather

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
                time = cityViewModel.currentDate
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
            PrintCitySection()
            Spacer(modifier = Modifier.weight(1f))
            BottomNavSection(
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintCitySection(
    cityViewModel: CityViewModel = hiltViewModel()
) {

    //Ось це переміщається в конструктор composable
//    val cityViewModel: CityViewModel = hiltViewModel()

    Box(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        var text by rememberSaveable {
            mutableStateOf("")
        }
        TextField(modifier = Modifier,
            shape = RoundedCornerShape(20.dp),
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            label = {
                Text(text = "City")
            },
            trailingIcon = {
                IconButton(onClick = {
                    cityViewModel.cityFromUI = text
                    cityViewModel.proceedIntent(CityScreenIntent.GetWeatherIntent)
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    cityViewModel.cityFromUI = text
                    cityViewModel.proceedIntent(CityScreenIntent.GetWeatherIntent)
                }
            )
        )
    }
}

@Composable
fun CityScreenCheckingUI(
    modifier: Modifier,
    navController: NavHostController
) {
    val cityViewModel: CityViewModel = hiltViewModel()

    val locationWeather = cityViewModel.weather

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
                time = cityViewModel.currentDate
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
            PrintCitySection()
            Spacer(modifier = Modifier.height(4.dp))
            ReconnectionSection()
            Spacer(modifier = Modifier.weight(1f))
            BottomNavSection(
                navController = navController
            )
        }
    }
}