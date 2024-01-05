package com.app.to_do.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.app.to_do.R
import com.app.to_do.components.PriorityDropDown
import com.app.to_do.data.models.Priority
import com.app.to_do.ui.theme.LARGE_PADDING
import com.app.to_do.ui.theme.MEDIUM_PADDING
import org.w3c.dom.Text

@Composable
fun TaskContent(
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(all = LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = title,
            onValueChange = {
                onTitleChanged(it)
            },
            label = { Text(text = stringResource(id = R.string.title)) },
            textStyle = MaterialTheme.typography.labelLarge,
            singleLine = true,
        )
        Divider(
            modifier = Modifier
                .height(MEDIUM_PADDING),
            color = MaterialTheme.colorScheme.background
        )
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxSize(),
            value = description,
            onValueChange = {
                onDescriptionChanged(it)
            },
            label = { Text(text = stringResource(id = R.string.description)) },
            textStyle = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
@Preview
fun PreviewTaskContent() {
    TaskContent(
        title = "",
        onTitleChanged = {},
        description = "",
        onDescriptionChanged = {},
        priority = Priority.NONE,
        onPrioritySelected = {}
    )
}