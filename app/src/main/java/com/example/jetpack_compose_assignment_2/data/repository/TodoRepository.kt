package com.example.jetpack_compose_assignment_2.data.repository

import com.example.jetpack_compose_assignment_2.data.local.TodoDao
import com.example.jetpack_compose_assignment_2.data.local.toTodo
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.data.model.toEntity
import com.example.jetpack_compose_assignment_2.data.remote.TodoApi
import com.example.jetpack_compose_assignment_2.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoApi: TodoApi,
    private val todoDao: TodoDao
) {

    fun getTodos(): Flow<Resource<List<Todo>>> = flow {
        emit(Resource.Loading())

        val cachedTodosEntities = todoDao.getAllTodos()
        if (cachedTodosEntities.isNotEmpty()) {
            val cachedTodos = cachedTodosEntities.map { it.toTodo() }
            emit(Resource.Success(cachedTodos))
        }

        try {
            val remoteTodos = todoApi.getTodos()
            todoDao.insertTodos(remoteTodos.map { it.toEntity() })
            emit(Resource.Success(remoteTodos))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't load data. Check your internet connection."))
        } catch (e: HttpException) {
            emit(Resource.Error("Server error: ${e.message}"))
        }
    }
}
