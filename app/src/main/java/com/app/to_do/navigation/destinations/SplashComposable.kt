package com.app.to_do.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.app.to_do.ui.screens.splash.SplashScreen
import com.app.to_do.utils.Constant


fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit,
) {
    composable(
        route = Constant.SPLASH_SCREEN,
        exitTransition = {
            slideOutVertically(
                targetOffsetY= {fullHeight -> -fullHeight},
                animationSpec = tween(300)
            )
        }
    ){
        SplashScreen(navigateToListScreen = navigateToListScreen)
    }
}



