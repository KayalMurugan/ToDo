package com.app.to_do.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.to_do.R
import com.app.to_do.data.models.Priority
import com.app.to_do.data.models.ToDoTask
import com.app.to_do.data.repository.DataStoreRepository
import com.app.to_do.data.repository.ToDoRepository
import com.app.to_do.ui.theme.MAX_TITLE_LENGTH
import com.app.to_do.utils.Action
import com.app.to_do.utils.RequestState
import com.app.to_do.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val id: MutableState<Int> = mutableIntStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)


    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _searchTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchTask: StateFlow<RequestState<List<ToDoTask>>> = _searchTask

    private val _allTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTask: StateFlow<RequestState<List<ToDoTask>>> = _allTask

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    init {
        getAllTask()
        readSortState()
    }

    fun searchDataBase(searchQuery: String) {
        _searchTask.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDataBase(searchQuery = "%$searchQuery%").collect { searchedTask ->
                    _searchTask.value = RequestState.Success(searchedTask)

                }
            }
        } catch (e: Exception) {
            _searchTask.value = RequestState.Error(e)
        }

        searchAppBarState.value = SearchAppBarState.TRIGGERED

    }

    val lowPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun persistSortState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }

    private fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map { Priority.valueOf(it) }
                    .collect {
                        _sortState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

    private fun getAllTask() {
        _allTask.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTask.collect {
                    _allTask.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTask.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val  selectedTask: MutableStateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int) {
        try {
            viewModelScope.launch {
                repository.getSelectedTask(taskId).collect { task ->
                    _selectedTask.value = task
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value,
            )
            repository.addTask(toDoTask)
        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value,
            )
            repository.updateTask(toDoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value,
            )
            repository.deleteTask(toDoTask)
        }
    }

    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTask()
        }
    }

    fun handleDatabaseAction(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }

            Action.UPDATE -> {
                updateTask()
            }

            Action.DELETE -> {
                deleteTask()
            }

            Action.DELETE_ALL -> {
                deleteAll()
            }

            Action.UNDO -> {
                addTask()
            }

            else -> {

            }
        }
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.value = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH)
            title.value = newTitle
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

    fun displayToast(context: Context) {
        Toast.makeText(
            context,
            context.getString(R.string.empty_msg),
            Toast.LENGTH_SHORT
        ).show()
    }
}