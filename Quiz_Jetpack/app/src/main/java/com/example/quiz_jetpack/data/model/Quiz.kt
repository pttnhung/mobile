package com.example.quiz_jetpack.data.model

data class Quiz(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)

data class QuizResponse(
    val questions: List<Quiz>
)

data class QuizState(
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val isAnswerSelected: Boolean = false,
    val selectedAnswer: Int? = null,
    val isQuizFinished: Boolean = false
) 