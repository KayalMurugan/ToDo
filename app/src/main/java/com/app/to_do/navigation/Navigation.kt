package com.app.to_do.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.app.to_do.navigation.destinations.listComposable
import com.app.to_do.navigation.destinations.splashComposable
import com.app.to_do.navigation.destinations.taskComposable
import com.app.to_do.ui.viewmodels.SharedViewModel
import com.app.to_do.utils.Constant.LIST_SCREEN
import com.app.to_do.utils.Constant.SPLASH_SCREEN

@Composable
fun SetUpNavigation(
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
){
    val screen = remember(navHostController) {
        Screens(navHostController = navHostController)
    }
    
    NavHost(navController = navHostController, startDestination = SPLASH_SCREEN) {
        splashComposable(
            navigateToListScreen = screen.splash
        )
        listComposable(
            navigateToTaskScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            navigateToListScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
    }
}