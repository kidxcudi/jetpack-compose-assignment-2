package com.example.jetpack_compose_assignment_2.ui.navigation

sealed class Screens(val route: String) {
    object TodoList : Screens("todo_list")
    object TodoDetail : Screens("todo_detail/{todoId}") {
        fun createRoute(todoId: Int) = "todo_detail/$todoId"
    }
}
