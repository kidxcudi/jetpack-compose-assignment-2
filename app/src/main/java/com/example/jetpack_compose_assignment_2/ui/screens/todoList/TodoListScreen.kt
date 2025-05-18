package com.example.jetpack_compose_assignment_2.ui.screens.todoList

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack_compose_assignment_2.data.model.Todo
import com.example.jetpack_compose_assignment_2.ui.viewModel.TodoViewModel
import com.example.jetpack_compose_assignment_2.util.Resource
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    viewModel: TodoViewModel,
    onTodoClick: (Int) -> Unit,
    currentUserId: Int
) {
    val todosState by viewModel.todos.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val systemUiController = rememberSystemUiController()
    val topBarColor = MaterialTheme.colorScheme.primary
    SideEffect {
        systemUiController.setStatusBarColor(
            color = topBarColor,
            darkIcons = false
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var successShown by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        val current = todosState
        if (!isRefreshing && current is Resource.Success && !current.fromCache && !successShown) {
            successShown = true
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Tasks refreshed successfully!",
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    LaunchedEffect(todosState) {
        if (todosState is Resource.Error) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = todosState.message ?: "Unable to load tasks. Check your internet.",
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    val tabTitles = listOf("All Tasks", "Incomplete", "Completed", "My Tasks")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("To-do list", color = MaterialTheme.colorScheme.onPrimary, fontWeight = Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, color = MaterialTheme.colorScheme.onPrimary) },
                        selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f)
                    )
                }
            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    successShown = false
                    viewModel.refreshTodos()
                }
            ) {
                when (val state = todosState) {
                    is Resource.Loading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    is Resource.Success -> {
                        val allTodos = state.data ?: emptyList()
                        val filtered = when (selectedTab) {
                            1 -> allTodos.filter { !it.completed }
                            2 -> allTodos.filter { it.completed }
                            3 -> allTodos.filter { it.userId == currentUserId }
                            else -> allTodos
                        }

                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(filtered) { todo ->
                                TodoItemCard(todo = todo, onClick = { onTodoClick(todo.id) })
                            }
                        }
                    }

                    is Resource.Error -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { viewModel.refreshTodos() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error loading tasks. Pull down to retry.", color = Color.Red)
                    }
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
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Task #${todo.id}", fontSize = 13.sp)
                Text(
                    text = if (todo.completed) "Done" else "Pending",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (todo.completed) Color(0xFF4CAF50) else Color(0xFFFF9800),
                    modifier = Modifier
                        .background(
                            color = if (todo.completed) Color(0x334CAF50) else Color(0x33FF9800),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = todo.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
