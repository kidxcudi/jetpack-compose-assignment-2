package com.example.jetpack_compose_assignment_2.data.remote

import com.example.jetpack_compose_assignment_2.data.remote.RemoteTodo
import retrofit2.http.GET

interface TodoApi {
    @GET("todos")
    suspend fun getTodos(): List<RemoteTodo>
}