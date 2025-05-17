package com.example.jetpack_compose_assignment_2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpack_compose_assignment_2.ui.screens.detail.TodoDetailScreen
import com.example.jetpack_compose_assignment_2.ui.screens.todoList.TodoListScreen
import com.example.jetpack_compose_assignment_2.ui.viewModel.TodoDetailViewModel
import com.example.jetpack_compose_assignment_2.ui.viewModel.TodoViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    todoViewModel: TodoViewModel,
    todoDetailViewModel: TodoDetailViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.TodoList.route
    ) {
        composable(Screens.TodoList.route) {
            TodoListScreen(
                viewModel = todoViewModel,
                onTodoClick = { todoId ->
                    navController.navigate("todoDetail/$todoId")
                }
            )
        }
        composable(
            route = Screens.TodoDetail.route,
            arguments = listOf(navArgument("todoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: 0
            TodoDetailScreen(
                todoId = todoId,
                viewModel = todoDetailViewModel,
                navController = navController
            )
        }
    }
}
