package com.app.to_do.ui.screens.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.to_do.R
import com.app.to_do.ui.theme.fabBackgroundColor
import com.app.to_do.ui.viewmodels.SharedViewModel
import com.app.to_do.utils.Action
import com.app.to_do.utils.SearchAppBarState
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {

    LaunchedEffect(key1 = action){
        sharedViewModel.handleDatabaseAction(action)
    }


    val allTasks by sharedViewModel.allTask.collectAsState()
    val searchedTasks by sharedViewModel.searchTask.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    val snackBarHostState = remember { SnackbarHostState() }


    DisplaySnackBar(
        snackBarHostState = snackBarHostState,
        taskTitle = sharedViewModel.title.value,
        action = action,
        onUndoClicked = {
            sharedViewModel.action.value = it
        },
        onComplete = {
            sharedViewModel.action.value = it
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            ListAppBar(
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState,
                sharedViewModel = sharedViewModel
            )
        },
        content = {
            Surface(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                ListContent(
                    allTasks = allTasks,
                    searchedTasks = searchedTasks,
                    searchAppBarState = searchAppBarState,
                    lowPriorityTasks = lowPriorityTasks,
                    highPriorityTasks = highPriorityTasks,
                    sortState = sortState,
                    onSwipeToDelete = { action, task ->
                        sharedViewModel.action.value = action
                        sharedViewModel.updateTaskFields(task)
                        snackBarHostState.currentSnackbarData?.dismiss()
                    },
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        },
        floatingActionButton = {
            ListFAB(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFAB(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        containerColor = MaterialTheme.colorScheme.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White,

            )
    }
}

@Composable
fun DisplaySnackBar(
    snackBarHostState: SnackbarHostState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = snackBarHostState.showSnackbar(
                    message = setMessage(action = action, taskTitle = taskTitle),
                    actionLabel = setActionLabel(action = action),
                    duration = SnackbarDuration.Short
                )
                Log.d("SnackBarResult", "ActionPerformed:: ${SnackbarResult.ActionPerformed}")
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    }

}

private fun setMessage(action: Action, taskTitle: String): String {
    return when (action) {
        Action.DELETE_ALL -> "All Tasks Removed"
        else -> "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action == Action.DELETE)
        "UNDO"
    else
        "OK"
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit,
) {

    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }

}
