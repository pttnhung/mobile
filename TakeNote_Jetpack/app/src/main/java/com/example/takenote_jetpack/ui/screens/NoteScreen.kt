package com.example.takenote_jetpack.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.takenote_jetpack.data.model.Note
import com.example.takenote_jetpack.ui.theme.TakeNote_JetpackTheme
import com.example.takenote_jetpack.ui.viewmodel.NoteViewModel
import com.example.takenote_jetpack.data.repository.NoteRepository
import com.example.takenote_jetpack.data.local.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(noteViewModel: NoteViewModel) {
    val notes by noteViewModel.notes.collectAsState()
    val error by noteViewModel.error.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Note") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Add a note") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp), // Có thể điều chỉnh cao tối thiểu
                maxLines = 5, // Hiển thị tối đa 5 dòng
                singleLine = false
            )

            Button(
                onClick = {
                    if (title.isNotBlank() || content.isNotBlank()) {
                        noteViewModel.addNote(title, content)
                        title = ""
                        content = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.height(16.dp))

            NoteList(
                notes = notes,
                onNoteClick = {},
                onDeleteClick = { note -> noteViewModel.deleteNote(note) }
            )

            error?.let {
                Text(
                    it,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun NoteList(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(notes) { note ->
            NoteItem(note = note, onNoteClick = onNoteClick, onDeleteClick = onDeleteClick)
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onNoteClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNoteClick(note) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = { onDeleteClick(note) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            Text(
                text = "Created: ${note.createdAt}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            note.emoji?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    TakeNote_JetpackTheme {
        NoteScreen(noteViewModel = NoteViewModel(
            NoteRepository(object : NoteDao {
                override fun getAllNotes(): Flow<List<Note>> = flowOf(emptyList())
                override suspend fun getNoteById(id: Long): Note? = null
                override suspend fun insertNote(note: Note): Long = 0
                override suspend fun updateNote(note: Note) {}
                override suspend fun deleteNote(note: Note) {}
                override suspend fun deleteNoteById(id: Long) {}
            })
        ))
    }
}
