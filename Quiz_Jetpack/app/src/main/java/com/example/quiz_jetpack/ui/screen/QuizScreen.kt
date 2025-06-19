package com.example.quiz_jetpack.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quiz_jetpack.data.model.Quiz
import com.example.quiz_jetpack.data.model.QuizState
import com.example.quiz_jetpack.ui.viewmodel.QuizViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable


@Composable
fun QuizQuestion(
    question: Quiz,
    quizState: QuizState,
    onAnswerSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Câu ${quizState.currentQuestionIndex + 1}/5",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        question.options.forEachIndexed { index, option ->
            val isSelected = quizState.selectedAnswer == index
            val isCorrect = index == question.correctAnswer
            val showResult = quizState.isAnswerSelected

            val backgroundColor = when {
                showResult && isCorrect -> androidx.compose.ui.graphics.Color(0xFF4CAF50) // Xanh lá
                showResult && isSelected -> androidx.compose.ui.graphics.Color(0xFFFFCDD2) // Đỏ nhạt
                showResult -> androidx.compose.ui.graphics.Color(0xFFFFEBEE) // Đỏ nhạt cho các đáp án còn lại
                isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(enabled = !quizState.isAnswerSelected) { onAnswerSelected(index) },
                shape = androidx.compose.foundation.shape.RectangleShape,
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor)
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                )
            }
        }

        if (quizState.isAnswerSelected) {
            Button(
                onClick = onNextQuestion,
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (quizState.isQuizFinished) "Kết thúc" else "Câu tiếp theo",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Text(
            text = "Số câu đúng: ${quizState.score}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}

@Composable
fun QuizResult(
    score: Int,
    totalQuestions: Int,
    onPlayAgain: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Your score: $score/$totalQuestions",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onPlayAgain,
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(
                text = "Play Again",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
} 