package com.example.jetpack_compose_assignment_2.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.repository.TodoRepository
import com.example.jetpack_compose_assignment_2.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    private val _todos = MutableStateFlow<Resource<List<Todo>>>(Resource.Loading())
    val todos: StateFlow<Resource<List<Todo>>> = _todos

    init {
        fetchTodos()
    }

    private fun fetchTodos() {
        viewModelScope.launch {
            repository.getTodos().collect { result ->
                _todos.value = result
            }
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun refreshTodos() {
        viewModelScope.launch {
            _isRefreshing.value = true
            fetchTodos()
            _isRefreshing.value = false
        }
    }
}
