package com.example.quiz_jetpack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_jetpack.data.model.Quiz
import com.example.quiz_jetpack.data.model.QuizState
import com.example.quiz_jetpack.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    private val _questions = MutableStateFlow<List<Quiz>>(emptyList())
    val questions: StateFlow<List<Quiz>> = _questions.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _questions.value = repository.getQuizQuestions()
        }
    }

    fun selectAnswer(answerIndex: Int) {
        val currentQuestion = questions.value.getOrNull(_quizState.value.currentQuestionIndex) ?: return
        val isCorrect = answerIndex == currentQuestion.correctAnswer

        _quizState.update { currentState ->
            currentState.copy(
                isAnswerSelected = true,
                selectedAnswer = answerIndex,
                score = if (isCorrect) currentState.score + 1 else currentState.score
            )
        }
    }

    fun moveToNextQuestion() {
        val currentIndex = _quizState.value.currentQuestionIndex
        val totalQuestions = questions.value.size

        if (currentIndex + 1 < totalQuestions) {
            _quizState.update { currentState ->
                currentState.copy(
                    currentQuestionIndex = currentIndex + 1,
                    isAnswerSelected = false,
                    selectedAnswer = null
                )
            }
        } else {
            _quizState.update { currentState ->
                currentState.copy(isQuizFinished = true)
            }
        }
    }

    fun resetQuiz() {
        _quizState.value = QuizState()
    }
} 
