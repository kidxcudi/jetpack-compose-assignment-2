package com.example.jetpack_compose_assignment_2.data.remote

import com.example.jetpack_compose_assignment_2.data.model.Todo

data class RemoteTodo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)

fun RemoteTodo.toTodo(): Todo = Todo(
    userId = userId,
    id = id,
    title = title,
    completed = completed,
    description = ""
)