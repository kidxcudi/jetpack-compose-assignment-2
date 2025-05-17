package com.example.jetpack_compose_assignment_2

import android.app.Application
import com.example.jetpack_compose_assignment_2.data.local.TodoDatabase

class TodoApplication : Application() {
    lateinit var database: TodoDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = TodoDatabase.getDatabase(this)
    }
}
