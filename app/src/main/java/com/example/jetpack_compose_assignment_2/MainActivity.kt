package com.example.jetpack_compose_assignment_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.jetpack_compose_assignment_2.data.remote.RetrofitInstance
import com.example.jetpack_compose_assignment_2.data.repository.TodoRepository
import com.example.jetpack_compose_assignment_2.ui.navigation.NavGraph
import com.example.jetpack_compose_assignment_2.ui.theme.Jetpackcomposeassignment2Theme
import com.example.jetpack_compose_assignment_2.ui.viewModel.TodoDetailViewModel
import com.example.jetpack_compose_assignment_2.ui.viewModel.TodoViewModel
import com.example.jetpack_compose_assignment_2.ui.viewModel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as TodoApplication
        val dao = app.database.todoDao()
        val api = RetrofitInstance.api

        val repository = TodoRepository(api, dao)
        val factory = ViewModelFactory(repository)
        val todoViewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]
        val todoDetailViewModel = ViewModelProvider(this, factory)[TodoDetailViewModel::class.java]

        setContent {
            Jetpackcomposeassignment2Theme {
                NavGraph(
                    todoViewModel = todoViewModel,
                    todoDetailViewModel = todoDetailViewModel
                )
            }
        }
    }
}
