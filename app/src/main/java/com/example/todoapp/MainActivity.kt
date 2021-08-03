package com.example.todoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.theme.TodoAppTheme

data class Todo(val text: String, var done: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    TopAppBar (title = { Text("Todos") })

                    TodoList()
                }
            }
        }
    }
}

@Composable
fun TodoList() {
    var todo by remember { mutableStateOf(TextFieldValue()) }

    var todos = remember {
        mutableStateListOf(
            Todo("Learn Android", false),
            Todo("Learn Jetpack Compose", true),
            Todo("Learn SwiftUI", false)
        )
    }

    Log.i("Main", "TodoList: $todos")

    fun addTodo() {
        if(todo.text.isNotEmpty()) {
            todos.add(Todo(todo.text, false))
            todo = TextFieldValue("")
        }
    }

    fun toggle(todo: Todo) {
        val index = todos.indexOf(todo)
        var t = todos[index]
        t.done = !t.done
        todos[index] = t
    }

    fun getTotalCompleted(): Int {
        return todos.filter { it.done }.size
    }

    fun clearCompleted() {
        for (todo in todos) {
            if(todo.done) {
                todos.remove(todo)
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = todo,
                onValueChange = { todo = it },
                placeholder = { Text("New Todo") }
            )

            GWButton("Add", Icons.Filled.Add, onClick = { addTodo() })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("You have ${getTotalCompleted()} completed of ${todos.size}",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(todos) { todo ->
                TodoItem(todo, onCheckedChange = { toggle(todo) })
            }
        }

        GWButton("Clear Completed", Icons.Filled.Delete, onClick = { clearCompleted() })
    }
}

@Composable
fun TodoItem(todo: Todo, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.padding(bottom = 16.dp)) {
        Checkbox(checked = todo.done, onCheckedChange = onCheckedChange)
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            todo.text,
            style = TextStyle(
                color = if (todo.done) Color.Gray else Color.Black,
                textDecoration = if (todo.done) TextDecoration.LineThrough else TextDecoration.None
            )
        )
    }
}

@Composable
fun GWButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoAppTheme {
        TodoList()
    }
}