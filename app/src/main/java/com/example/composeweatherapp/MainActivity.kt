package com.example.composeweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.VectorConverter
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import com.example.composeweatherapp.ui.theme.ComposeWeatherAppTheme
import com.example.composeweatherapp.ui.theme.backgroundColor
import com.example.composeweatherapp.ui.theme.boxesColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWeatherAppTheme {
                MyWeatherApp(modifier = Modifier)
            }
        }
    }
}

// smth


@Composable
fun MyWeatherApp(modifier: Modifier) {
    Surface(
        modifier.fillMaxSize()
    ) {
        Column(Modifier.background(backgroundColor)) {
            LocationSection(city = "Berdychiv", time = "19 September")
            TemperatureSection(temperature = "19 °C")
            BoxesSection(wind = "19km/h", humidity = "64%", clouds = "overcast clouds")
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
                painter = painterResource(id = R.drawable.cludy),
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
                Row(Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,) {
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
                Row(Modifier.padding(horizontal = 11.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,) {
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


@Preview
@Composable
fun MyWeatherAppPreview() {
    ComposeWeatherAppTheme {
        MyWeatherApp(modifier = Modifier)
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val expanded = rememberSaveable { mutableStateOf(false) }
    val extraPadding by animateDpAsState(
        if (expanded.value) {
            40.dp
        } else {
            0.dp
        }, animationSpec = spring(
            Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ), label = ""
    )

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "Hello,")
                Text(text = name)
            }
            ElevatedButton(onClick = { expanded.value = !expanded.value }) {
                Text(
                    if (expanded.value) {
                        "Show more"
                    } else {
                        "Show less"
                    }
                )
            }
        }
    }
}

@Composable
fun Greetings(modifier: Modifier = Modifier, names: List<String> = List(1000) { "$it" }) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier, names: List<String> = listOf("World", "Compose")) {

    var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }

    Surface(
        modifier = modifier,
    ) {
        if (shouldShowOnBoarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnBoarding = false })
        } else {
            Greetings()
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    ComposeWeatherAppTheme {
        Greetings()
    }
}


@Composable
fun MyAppPreview() {
    ComposeWeatherAppTheme {
        MyApp(Modifier.fillMaxSize())
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: This state should be hoisted
    var shouldShowOnboarding by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun OnboardingPreview() {
    ComposeWeatherAppTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}