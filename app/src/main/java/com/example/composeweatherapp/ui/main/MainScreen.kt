package com.example.composeweatherapp.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composeweatherapp.R
import com.example.composeweatherapp.ui.theme.ComposeWeatherAppTheme
import com.example.composeweatherapp.ui.theme.boxesColor
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    modifier: Modifier
) {

    val locationWeather = mainViewModel.weather

    val gradientColorList = listOf(
        Color(0xFFFBAB7E),
        Color(0xFFF7CE68)
    )

    val sdf = SimpleDateFormat("dd.MM")

    Surface(
        modifier.fillMaxSize()
    ) {
        Column(
            Modifier.background(
                GradientBackgroundBrush(
                    isVerticalGradient = true,
                    colors = gradientColorList
                )
            )
        ) {
            LocationSection(city = locationWeather.value.name, time = sdf.format(Date()))
            TemperatureSection(temperature = locationWeather.value.temp + " °C")
            BoxesSection(
                wind = locationWeather.value.speed + "km/h",
                humidity = locationWeather.value.humidity + "%",
                clouds = locationWeather.value.description
            )
        }
    }
}

@Composable
fun LocationSection(city: String, time: String) {
    Box(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
            Text(
                text = city,
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF313341)
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = time,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF9A938C)
                )
            )
        }
    }
}

@Composable
fun TemperatureSection(temperature: String) {
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
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF313341),
                )
            )

        }
    }
}

@Composable
fun BoxesSection(wind: String, humidity: String, clouds: String) {
    Box() {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 15.dp)) {
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
                            painter = painterResource(id = R.drawable.wind),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text("Wind")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(modifier = Modifier.padding(end = 16.dp), text = wind)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
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
                            painter = painterResource(id = R.drawable.humid),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text("Humidity")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(modifier = Modifier.padding(end = 16.dp), text = humidity)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
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
                            painter = painterResource(id = R.drawable.cloud),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text("Clouds")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(modifier = Modifier.padding(end = 16.dp), text = clouds)
                    }
                }
            }
        }
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


@Preview
@Composable
fun MyWeatherAppPreview() {
    ComposeWeatherAppTheme {
        MainScreen(modifier = Modifier)
    }
}
