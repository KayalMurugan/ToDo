package com.app.to_do.ui.screens.task

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.LocalContext
import com.app.to_do.data.models.Priority
import com.app.to_do.data.models.ToDoTask
import com.app.to_do.ui.viewmodels.SharedViewModel
import com.app.to_do.utils.Action

@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {

    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    val context = LocalContext.current
    
    //BackHandler(onBackPressed = {})
    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }
    
    Scaffold(
        topBar = {
            TaskAppBar(
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION) {
                        navigateToListScreen(action)
                    } else {
                        if (sharedViewModel.validateFields()) {
                            navigateToListScreen(action)
                        } else {
                            sharedViewModel.displayToast(context)
                        }
                    }
                },
                selectedTask = selectedTask
            )
        },
        content = {
            Surface(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                TaskContent(
                    title = title,
                    onTitleChanged = { it ->
                        sharedViewModel.updateTitle(it)
                    },
                    description = description,
                    onDescriptionChanged = { it ->
                        sharedViewModel.description.value = it
                    },
                    priority = priority,
                    onPrioritySelected = { it ->
                        sharedViewModel.priority.value = it
                    }
                )
            }
        }
    )
}

//@Composable
//fun BackHandler(
//    backDispatcher: OnBackPressedDispatcher? =
//        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
//    onBackPressed: () -> Unit
//){
//    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
//
//    val backCallBack = remember{
//        object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                currentOnBackPressed()
//            }
//
//        }
//    }
//
//    DisposableEffect(key1 = backDispatcher) {
//        backDispatcher?.addCallback(backCallBack)
//        onDispose {
//            backCallBack.remove()
//        }
//    }
//
//}
