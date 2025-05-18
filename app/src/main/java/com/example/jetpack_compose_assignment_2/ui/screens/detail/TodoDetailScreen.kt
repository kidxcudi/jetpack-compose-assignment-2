package com.example.jetpack_compose_assignment_2.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jetpack_compose_assignment_2.ui.viewModel.TodoDetailViewModel
import com.example.jetpack_compose_assignment_2.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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

    val systemUiController = rememberSystemUiController()
    val topBarColor = MaterialTheme.colorScheme.primary
    SideEffect {
        systemUiController.setStatusBarColor(
            color = topBarColor,
            darkIcons = false
        )
    }

    val todo by viewModel.todo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(R.drawable.back), contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (todo != null) {
                val currentTodo = todo!!
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(20.dp, 6.dp, 16.dp, 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Task #${currentTodo.id}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (currentTodo.completed)
                            Color(0x334CAF50) else Color(0x33FF9800)
                    ) {
                        Text(
                            text = if (currentTodo.completed) "Done" else "Pending",
                            color = if (currentTodo.completed) Color(0xFF4CAF50) else Color(0xFFFF9800),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (val currentTodo = todo) {
                    null -> CircularProgressIndicator()
                    else -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = MaterialTheme.shapes.medium,
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    "Title",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    currentTodo.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Divider(color = MaterialTheme.colorScheme.outline)

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    "Description",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    currentTodo.description.ifBlank { "No description provided." },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
