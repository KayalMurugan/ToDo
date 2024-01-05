package com.app.to_do.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.app.to_do.R
import com.app.to_do.ui.theme.fabBackgroundColor
import com.app.to_do.ui.theme.splashScreenBackground
import com.app.to_do.ui.theme.taskItemTitleColor
import com.app.to_do.utils.Constant
import com.app.to_do.utils.Constant.SPLASH_SCREEN_DELAY
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToListScreen: () -> Unit,
) {
    LaunchedEffect(key1 = true){
        delay(3001)
        navigateToListScreen()
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.splashScreenBackground),
        contentAlignment = Alignment.Center
    ){
        Row {
            Text(
                modifier = Modifier.weight(8f, true),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.app_name_2),
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}