package com.example.jetpack_compose_assignment_2.data.model

import com.example.jetpack_compose_assignment_2.data.local.TodoEntity

data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean,
    val description: String = ""
)

fun Todo.toEntity(): TodoEntity = TodoEntity(
    id = id,
    userId = userId,
    title = title,
    completed = completed,
    description = description
)