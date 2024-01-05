package com.app.to_do.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.to_do.ui.screens.list.ListScreen
import com.app.to_do.ui.viewmodels.SharedViewModel
import com.app.to_do.utils.Action
import com.app.to_do.utils.Constant
import com.app.to_do.utils.Constant.LIST_ARGUMENT_KEY
import com.app.to_do.utils.Constant.LIST_SCREEN
import com.app.to_do.utils.toAction

fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){navBackStackEntry ->
        val action=navBackStackEntry.arguments!!.getString(LIST_ARGUMENT_KEY).toAction()

        var myAction by rememberSaveable {
            mutableStateOf(Action.NO_ACTION)
        }

        LaunchedEffect(key1 = myAction){
            if (action != myAction){
                myAction = action
                sharedViewModel.action.value = action
            }

        }
        val databaseAction by sharedViewModel.action

        ListScreen(
            action = databaseAction,
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = sharedViewModel
        )
    }
}



