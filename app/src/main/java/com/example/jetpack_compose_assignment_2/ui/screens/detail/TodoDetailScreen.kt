package com.example.jetpack_compose_assignment_2.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetpack_compose_assignment_2.ui.viewModel.TodoDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    todoId: Int,
    viewModel: TodoDetailViewModel,
    navController: NavController
) {
    LaunchedEffect(todoId) {
        viewModel.loadTodoById(todoId)
    }

    val todo by viewModel.todo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        todo?.let { todo ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {
                Text("Title:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(todo.title, fontSize = 18.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Description:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(todo.description.ifBlank { "No description provided." }, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Status:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    if (todo.completed) "Done" else "Pending",
                    color = if (todo.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp
                )
            }
        }

    }
}
