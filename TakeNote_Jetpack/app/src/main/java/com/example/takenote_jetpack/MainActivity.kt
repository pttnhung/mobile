package com.example.takenote_jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.takenote_jetpack.ui.theme.TakeNote_JetpackTheme
import com.example.takenote_jetpack.ui.viewmodel.NoteViewModel
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.example.takenote_jetpack.ui.screens.NoteScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TakeNote_JetpackTheme {
                NoteScreen(noteViewModel = noteViewModel)
            }
        }
    }
}

@Composable
fun NoteScreen(noteViewModel: NoteViewModel) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "Note Screen Content",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TakeNote_JetpackTheme {
    }
}