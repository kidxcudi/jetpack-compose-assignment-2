package com.example.jetpack_compose_assignment_2.ui.screens.todoList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.ui.viewModel.TodoViewModel
import com.example.jetpack_compose_assignment_2.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    viewModel: TodoViewModel,
    onTodoClick: (Int) -> Unit
) {
    val todosState by viewModel.todos.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("To-do list") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                listOf("All tasks", "Incomplete", "Completed").forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (todosState) {
                is Resource.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    val allTodos = (todosState as Resource.Success).data ?: emptyList()
                    val filteredTodos = when (selectedTab) {
                        1 -> allTodos.filter { !it.completed }
                        2 -> allTodos.filter { it.completed }
                        else -> allTodos
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(filteredTodos) { todo ->
                            TodoItemCard(todo = todo, onClick = { onTodoClick(todo.id) })
                        }
                    }
                }

                is Resource.Error -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error loading tasks", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun TodoItemCard(todo: Todo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (todo.completed) "Done" else "Pending",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (todo.completed) Color(0xFF4CAF50) else Color(0xFFFF9800),
                modifier = Modifier
                    .background(
                        if (todo.completed) Color(0x334CAF50) else Color(0x33FF9800),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = todo.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
