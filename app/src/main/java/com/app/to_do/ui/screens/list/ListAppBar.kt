package com.app.to_do.ui.screens.list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.to_do.R
import com.app.to_do.data.models.Priority
import com.app.to_do.ui.theme.topAppBarBackgroundColor
import com.app.to_do.ui.theme.topAppBarContentColor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.app.to_do.components.DisplayAlertDialog
import com.app.to_do.components.PriorityItems
import com.app.to_do.ui.theme.LARGE_PADDING
import com.app.to_do.ui.theme.TOP_APP_BAR_HEIGHT
import com.app.to_do.ui.theme.Typography
import com.app.to_do.ui.viewmodels.SharedViewModel
import com.app.to_do.utils.Action
import com.app.to_do.utils.SearchAppBarState
import com.app.to_do.utils.TrailingIconState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.searchAppBarState.value =
                        SearchAppBarState.OPENED
                },
                onSortClicked = {
                    sharedViewModel.persistSortState(it)
                },
                onDeleteAllClicked = {
                    sharedViewModel.action.value = Action.DELETE_ALL
                }
            )
        }

        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { newText ->
                    sharedViewModel.searchTextState.value = newText
                },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value =
                        SearchAppBarState.CLOSED
                    sharedViewModel.searchTextState.value = ""
                },
                onSearchClicked = {
                    sharedViewModel.searchDataBase(it)
                })
        }

    }


}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.listing_title),
                color = MaterialTheme.colorScheme.topAppBarContentColor,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
        },
        actions = {
            ListAppBarAction(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllClicked = onDeleteAllClicked
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.topAppBarBackgroundColor,
        )
    )
}

@Composable
fun ListAppBarAction(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = stringResource(
            id = R.string.deleted_all_task,
        ),
        message = stringResource(
            id = R.string.delete_all_task_confirmation,
        ),
        openDialog = openDialog,
        closeDialog = {
            openDialog = false
        },
        onYesClicked = {
            onDeleteAllClicked()
        }
    )
    SearchBarAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllClicked = {
        openDialog = true
    })
}

@Composable
fun SearchBarAction(
    onSearchClicked: () -> Unit
) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_tasks),
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            expanded = true
        }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_fillter_list),
            contentDescription = stringResource(id = R.string.sort_tasks),
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Priority.values().slice(setOf(0,2,3)).forEach { priority ->
                DropdownMenuItem(
                    text = {
                        PriorityItems(priority = priority)
                    },
                    onClick = {
                        expanded = false
                        onSortClicked(priority)
                    })
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            expanded = true
        }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = stringResource(id = R.string.sort_tasks),
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        modifier = Modifier.padding(start = LARGE_PADDING),
                        text = stringResource(id = R.string.delete_all_task),
                        style = Typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                onClick = {
                    onDeleteAllClicked()
                    expanded = false
                })
        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        tonalElevation = 0.dp,
        color = MaterialTheme.colorScheme.topAppBarBackgroundColor,
    ) {

        var trailingIconState by remember {
            mutableStateOf(TrailingIconState.READY_TO_DELETE)
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = stringResource(id = R.string.search),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.topAppBarContentColor,
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier.alpha(0.5f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = MaterialTheme.colorScheme.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text == "") {
                            onTextChange("")
                            trailingIconState = TrailingIconState.READY_TO_CLOSE
                        }
                        when (trailingIconState) {
                            TrailingIconState.READY_TO_DELETE -> {
                                onTextChange("")
                                trailingIconState = TrailingIconState.READY_TO_CLOSE
                            }

                            TrailingIconState.READY_TO_CLOSE -> {
                                if (text.isNotEmpty()) {
                                    onTextChange("")
                                } else {
                                    onCloseClicked()
                                    trailingIconState = TrailingIconState.READY_TO_DELETE
                                }
                            }
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close_icon),
                        tint = MaterialTheme.colorScheme.topAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
        )
    }
}

@Preview
@Composable
private fun DefaultListPreview() {
    DefaultListAppBar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteAllClicked = {}
    )
}

@Preview
@Composable
private fun SearchBarPreview() {
    SearchAppBar(
        text = "",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {})
}