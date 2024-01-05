package com.app.to_do.navigation

import androidx.navigation.NavHostController
import com.app.to_do.utils.Action
import com.app.to_do.utils.Constant.LIST_SCREEN
import com.app.to_do.utils.Constant.SPLASH_SCREEN

class Screens(navHostController: NavHostController) {

    val splash: () -> Unit = {
        navHostController.navigate(route = "list/${Action.NO_ACTION.name}") {
            popUpTo(SPLASH_SCREEN) { inclusive = true }
        }
    }
    val list: (Action) -> Unit = { action ->
        navHostController.navigate(route = "list/${action.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }

    val task: (Int) -> Unit = { taskId ->
        navHostController.navigate(route = "task/${taskId}")
    }
}