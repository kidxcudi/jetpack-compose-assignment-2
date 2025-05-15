package com.example.jetpack_compose_assignment_2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jetpack_compose_assignment_2.data.model.Todo

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean,
    val description: String = ""
)

fun TodoEntity.toTodo(): Todo = Todo(
    id = id,
    userId = userId,
    title = title,
    completed = completed,
    description = description
)