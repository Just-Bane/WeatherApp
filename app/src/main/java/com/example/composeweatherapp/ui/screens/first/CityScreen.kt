package com.example.composeweatherapp.ui.screens.first

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.navigation.compose.rememberNavController
import com.example.composeweatherapp.R
import com.example.composeweatherapp.ui.screens.main.BoxesSection
import com.example.composeweatherapp.ui.screens.main.GradientBackgroundBrush
import com.example.composeweatherapp.ui.screens.main.LocationSection
import com.example.composeweatherapp.ui.screens.main.TemperatureSection
import com.example.composeweatherapp.ui.screens.main.WeatherUIData
import com.example.composeweatherapp.ui.theme.ComposeWeatherAppTheme
import com.example.composeweatherapp.ui.theme.gradientColorList

@Composable
fun CityScreen(modifier: Modifier) {
    val cityViewModel: CityViewModel = hiltViewModel()

    val navController = rememberNavController()

    when (cityViewModel.screenState.value) {
        is CityViewModel.CityScreenState.WriteTheCity -> {
            CityScreenUI(modifier = Modifier)
        }

        CityViewModel.CityScreenState.CorrectCityWritten -> {
            CityScreenUI(modifier = Modifier)
            cityViewModel.screenState.value = CityViewModel.CityScreenState.WriteTheCity
        }

        CityViewModel.CityScreenState.WrongCityWritten -> {
            CityScreenUI(modifier = Modifier)
            EnableDialog()
        }

        CityViewModel.CityScreenState.NoInternet -> {
            navController.navigate("internet")
        }
    }
}


@Composable
fun CityScreenUI(
    modifier: Modifier
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
        }
    }
}

@Composable
fun EnableDialog() {

    val cityViewModel: CityViewModel = hiltViewModel()

    val openDialog = rememberSaveable {
        mutableStateOf(true)
    }
    openDialog.value = true
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                cityViewModel.screenState.value = CityViewModel.CityScreenState.WriteTheCity
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        cityViewModel.screenState.value = CityViewModel.CityScreenState.WriteTheCity
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
fun FirstScreenCheck(modifier: Modifier) {
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
                city = "Berdychiv",
                time = "10.10"
            )
            TemperatureSection(
                temperature = "10"
            )
            BoxesSection(
                weatherUIData = listOf(
                    WeatherUIData(
                        title = "Wind",
                        iconId = R.drawable.wind,
                        weatherData = "15 km/h"
                    ),
                    WeatherUIData(
                        title = "Humidity",
                        iconId = R.drawable.humid,
                        weatherData = "70 %"
                    ),
                    WeatherUIData(
                        title = "Clouds",
                        iconId = R.drawable.cloud,
                        weatherData = "rain"
                    )
                )
            )
            PrintCitySection()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintCitySection() {

    val cityViewModel: CityViewModel = hiltViewModel()


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
                    cityViewModel.proceedIntent(
                        CityViewModel.CityScreenIntent.GetWeatherIntent,
                        text
                    )
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
                    cityViewModel.proceedIntent(
                        CityViewModel.CityScreenIntent.GetWeatherIntent,
                        text
                    )
                }
            )
        )
    }
}

@Preview
@Composable
fun FirstScreenPreview() {
    ComposeWeatherAppTheme {
        FirstScreenCheck(modifier = Modifier)
    }
}