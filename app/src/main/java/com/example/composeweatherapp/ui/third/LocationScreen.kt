package com.example.composeweatherapp.ui.third

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.composeweatherapp.R
import com.example.composeweatherapp.ui.main.BoxesSection
import com.example.composeweatherapp.ui.main.GradientBackgroundBrush
import com.example.composeweatherapp.ui.main.LocationSection
import com.example.composeweatherapp.ui.main.TemperatureSection
import com.example.composeweatherapp.ui.main.WeatherUIData
import com.example.composeweatherapp.ui.theme.ComposeWeatherAppTheme
import com.example.composeweatherapp.ui.theme.gradientColorList

@Composable
fun LocationScreen(
    modifier: Modifier
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintLatLonSection() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var textLat by rememberSaveable {
            mutableStateOf("")
        }
        var textLon by rememberSaveable {
            mutableStateOf("")
        }

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
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
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
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // request
                }
            )
        )
    }
}

@Composable
fun ThirdScreenCheck(modifier: Modifier) {
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
            PrintLatLonSection()
        }
    }
}

@Preview
@Composable
fun FirstScreenPreview() {
    ComposeWeatherAppTheme {
        ThirdScreenCheck(modifier = Modifier)
    }
}