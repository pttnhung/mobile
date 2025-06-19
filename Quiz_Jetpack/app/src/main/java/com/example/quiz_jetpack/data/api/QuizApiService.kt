package com.example.quiz_jetpack.data.api

import com.example.quiz_jetpack.data.model.QuizResponse
import retrofit2.http.GET

interface QuizApiService {
    @GET("quiz.json")
    suspend fun getQuizQuestions(): QuizResponse
} 