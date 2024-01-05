package com.app.to_do.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val LightGrey = Color(0xFFFCFCFC)
val MediumGrey = Color(0xFF9C9C9C)
val DarkGrey = Color(0xFF141414)

val HighPriorityColor = Color(0xFFF44336)
val MediumPriorityColor = Color(0xFFFF9800)
val LowPriorityColor = Color(0xFF4CAF50)
val NonePriorityColor = Color(0xFFFCFBFB)

val ColorScheme.splashScreenBackground: Color
    @Composable
    get() = if(isSystemInDarkTheme()) Color.Black else Purple80


val ColorScheme.taskItemTitleColor: Color
    @Composable
    get() = if(isSystemInDarkTheme()) LightGrey else DarkGrey


val ColorScheme.taskItemBackgroundColor: Color
    @Composable
    get() = if(isSystemInDarkTheme()) DarkGrey else Color.White

val ColorScheme.fabBackgroundColor: Color
    @Composable
    get() = if(isSystemInDarkTheme()) Purple40 else Purple80

val ColorScheme.topAppBarContentColor: Color
@Composable
get() = if(isSystemInDarkTheme()) LightGrey else Color.White

val ColorScheme.topAppBarBackgroundColor: Color
    @Composable
    get() = if(isSystemInDarkTheme()) Color.Black else Purple40
