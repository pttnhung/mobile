package com.example.quiz_jetpack.data.repository

import com.example.quiz_jetpack.data.api.QuizApiService
import com.example.quiz_jetpack.data.model.Quiz
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val apiService: QuizApiService
) {
    suspend fun getQuizQuestions(): List<Quiz> {
        return try {
            apiService.getQuizQuestions().questions
        } catch (e: Exception) {
            emptyList()
        }
    }
} 