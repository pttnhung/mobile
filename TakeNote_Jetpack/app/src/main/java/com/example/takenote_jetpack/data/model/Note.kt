package com.example.takenote_jetpack.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val emoji: String? = null,  // For storing emoji reactions
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) 