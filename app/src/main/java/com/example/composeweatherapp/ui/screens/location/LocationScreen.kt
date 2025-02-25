package com.example.composeweatherapp.ui.screens.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.composeweatherapp.R
import com.example.composeweatherapp.core.LOCATION_SCREEN
import com.example.composeweatherapp.ui.nav.NavigationScreens
import com.example.composeweatherapp.ui.screens.main.BottomNavSection
import com.example.composeweatherapp.ui.screens.main.BoxesSection
import com.example.composeweatherapp.ui.screens.main.EnableDialog
import com.example.composeweatherapp.ui.screens.main.GradientBackgroundBrush
import com.example.composeweatherapp.ui.screens.main.LocationSection
import com.example.composeweatherapp.ui.screens.main.ReconnectionSection
import com.example.composeweatherapp.ui.screens.main.TemperatureSection
import com.example.composeweatherapp.ui.screens.main.WeatherUIData
import com.example.composeweatherapp.ui.theme.ComposeWeatherAppTheme
import com.example.composeweatherapp.ui.theme.gradientColorList

@Composable
fun LocationScreen(modifier: Modifier, navController: NavHostController) {

    val locationViewModel: LocationViewModel = hiltViewModel()

    val state = locationViewModel.viewState.collectAsState()
    val event = locationViewModel.event.collectAsState()

    when (state.value) {
        LocationScreenState.WriteTheLocation -> {
            LocationScreenUI(modifier = Modifier, navController = navController)
        }

        LocationScreenState.CorrectLocationWritten -> {
            LocationScreenUI(modifier = Modifier, navController = navController)
            locationViewModel.changeToDefaultState()
        }

        LocationScreenState.WrongLocationWritten -> {
            LocationScreenUI(modifier = Modifier, navController = navController)
            EnableDialog(LOCATION_SCREEN)
        }

        LocationScreenState.NoInternet -> {
            LocationCheckingScreenUI(modifier = Modifier, navController = navController)
        }
    }

    when (event.value) {
        LocationScreenEvents.NavigateToInternetScreen -> {
            navController.navigate(NavigationScreens.Internet.route)
        }
        null -> {}
    }
}


@Composable
fun LocationScreenUI(
    modifier: Modifier,
    navController: NavHostController
) {
    val locationViewModel: LocationViewModel = hiltViewModel()

    val locationWeather = locationViewModel.weather

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
                time = locationViewModel.currentDate
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
            PrintLatLonSection()
            Spacer(modifier = Modifier.weight(1f))
            BottomNavSection(
                navController = navController
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintLatLonSection() {

    val context = LocalContext.current

    val locationViewModel: LocationViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var textLat by rememberSaveable {
                mutableStateOf("")
            }
            var textLon by rememberSaveable {
                mutableStateOf("")
            }
            Column(
                modifier = Modifier.padding(end = 8.dp),
            ) {
                TextField(modifier = Modifier,
                    value = textLat,
                    onValueChange = { newText ->
                        textLat = newText
                    },
                    label = {
                        Text(text = context.getString(R.string.lat))
                    },
                    colors = TextFieldDefaults.textFieldColors(

                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // request
                        }
                    )
                )
                Spacer(Modifier.height(8.dp))
                TextField(modifier = Modifier,
                    value = textLon,
                    onValueChange = { newText ->
                        textLon = newText
                    },
                    label = {
                        Text(text = context.getString(R.string.lon))
                    },
                    colors = TextFieldDefaults.textFieldColors(

                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // request
                        }
                    )
                )
            }
            IconButton(
                onClick = {
                    locationViewModel.latFromUI = textLat
                    locationViewModel.lonFromUI = textLon
                    locationViewModel.proceedIntent(LocationScreenIntent.GetWeatherIntent)
                },
                modifier = Modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        }
    }
}

@Composable
fun LocationCheckingScreenUI(
    modifier: Modifier,
    navController: NavHostController
) {
    val locationViewModel: LocationViewModel = hiltViewModel()

    val locationWeather = locationViewModel.weather

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
                time = locationViewModel.currentDate
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
            PrintLatLonSection()
            Spacer(modifier = Modifier.height(4.dp))
            ReconnectionSection()
            Spacer(modifier = Modifier.weight(1f))
            BottomNavSection(
                navController = navController
            )
        }
    }
}

