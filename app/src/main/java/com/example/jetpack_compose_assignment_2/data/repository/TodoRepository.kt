package com.example.jetpack_compose_assignment_2.data.repository

import com.example.jetpack_compose_assignment_2.data.local.TodoDao
import com.example.jetpack_compose_assignment_2.data.local.toTodo as entityToTodo
import com.example.jetpack_compose_assignment_2.data.remote.toTodo as remoteToTodo
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.model.toEntity
import com.example.jetpack_compose_assignment_2.data.remote.TodoApi
import com.example.jetpack_compose_assignment_2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class TodoRepository(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) {
    fun getTodos(): Flow<Resource<List<Todo>>> = flow {
        emit(Resource.Loading())

        val cachedTodos = todoDao.getAllTodos().map { it.entityToTodo() }
        if (cachedTodos.isNotEmpty()) {
            emit(Resource.Success(cachedTodos, fromCache = true))
        }

        try {
            val remoteTodos = todoApi.getTodos()
            val todos = remoteTodos.map { it.remoteToTodo() }
            todoDao.insertTodos(todos.map { it.toEntity() })
            emit(Resource.Success(todos, fromCache = false))
        } catch (e: IOException) {
            if (cachedTodos.isEmpty()) {
                emit(Resource.Error("Couldn't load data. Check your internet connection."))
            }
        } catch (e: HttpException) {
            if (cachedTodos.isEmpty()) {
                emit(Resource.Error("Server error: ${e.message}"))
            }
        }
    }

    suspend fun getTodoById(id: Int): Todo? {
        return todoDao.getTodoById(id)?.entityToTodo()
    }
}