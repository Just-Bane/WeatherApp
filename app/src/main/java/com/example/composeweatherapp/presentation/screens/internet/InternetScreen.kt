package com.example.composeweatherapp.presentation.screens.internet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.composeweatherapp.R
import com.example.composeweatherapp.presentation.nav.BottomBarScreen
import com.example.composeweatherapp.presentation.nav.NavigationScreens
import com.example.composeweatherapp.presentation.screens.main.GradientBackgroundBrush
import com.example.composeweatherapp.presentation.theme.boxesColor
import com.example.composeweatherapp.presentation.theme.gradientColorList

@Composable
fun InternetScreen(modifier: Modifier, navController: NavController) {

    val internetViewModel: InternetViewModel = hiltViewModel()

    val state = internetViewModel.viewState.collectAsState()
    val event = internetViewModel.event.collectAsState()

    when (state.value) {
        InternetScreenState.Default -> {
            InternetScreenUI(modifier = Modifier)
        }

        InternetScreenState.TrueInternet -> {
        }

        InternetScreenState.FalseInternet -> {
            InternetScreenUI(modifier = Modifier)
        }
    }

    when (event.value) {
        InternetScreenEvents.NavigateToHomeScreen -> {
            navController.navigate(NavigationScreens.Home.route)
        }
        null -> {}
    }
}

@Composable
fun InternetScreenUI(modifier: Modifier) {
    val internetViewModel: InternetViewModel = hiltViewModel()

    val context = LocalContext.current

    val fontFamily = FontFamily(
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_bold, FontWeight.Bold)
    )


    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.background(
                GradientBackgroundBrush(
                    isVerticalGradient = true,
                    colors = gradientColorList
                )
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color.Red
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = context.getString(R.string.no_internet),
                color = Color.Black,
                fontSize = 30.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    internetViewModel.proceedIntent(InternetScreenIntent.CheckTheInternetIntent)
                },
                colors = ButtonDefaults.buttonColors(boxesColor),
                shape = RoundedCornerShape(20.dp),
            ) {
                Text(
                    text = context.getString(R.string.retry),
                    color = Color.DarkGray,
                    fontSize = 30.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}