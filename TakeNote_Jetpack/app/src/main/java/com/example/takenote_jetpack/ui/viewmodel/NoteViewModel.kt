package com.example.takenote_jetpack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takenote_jetpack.data.model.Note
import com.example.takenote_jetpack.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            repository.getAllNotes()
                .catch { e ->
                    _error.value = e.message
                }
                .collect { notes ->
                    _notes.value = notes
                }
        }
    }

    fun addNote(title: String, content: String, emoji: String? = null) {
        viewModelScope.launch {
            try {
                val note = Note(
                    title = title,
                    content = content,
                    emoji = emoji
                )
                repository.insertNote(note)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.updateNote(note)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.deleteNote(note)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteNoteById(id)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
} 