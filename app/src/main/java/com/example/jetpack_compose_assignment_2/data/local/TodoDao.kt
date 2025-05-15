package com.example.jetpack_compose_assignment_2.data.local

import androidx.room.*

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos")
    suspend fun getAllTodos(): List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(todos: List<TodoEntity>)

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Int): TodoEntity?
}
