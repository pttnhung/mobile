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
fun QuizScreen(
    viewModel: QuizViewModel = viewModel()
) {
    val questions by viewModel.questions.collectAsState()
    val quizState by viewModel.quizState.collectAsState()

    val currentQuestion = questions.getOrNull(quizState.currentQuestionIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (quizState.isQuizFinished) {
            QuizResult(
                score = quizState.score,
                totalQuestions = questions.size,
                onPlayAgain = viewModel::resetQuiz
            )
        } else if (currentQuestion != null) {
            QuizQuestion(
                question = currentQuestion,
                quizState = quizState,
                onAnswerSelected = viewModel::selectAnswer,
                onNextQuestion = viewModel::moveToNextQuestion
            )
        } else {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun QuizQuestion(
    question: Quiz,
    quizState: QuizState,
    onAnswerSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Câu ${quizState.currentQuestionIndex + 1}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = question.question,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        question.options.forEachIndexed { index, option ->
            var isHovered by remember { mutableStateOf(false) }
            val isSelected = quizState.selectedAnswer == index
            val isCorrect = index == question.correctAnswer
            val showResult = quizState.isAnswerSelected

            val backgroundColor = when {
                isHovered && !showResult -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                showResult && isCorrect -> MaterialTheme.colorScheme.primaryContainer
                showResult && isSelected -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .then(
                        if (!quizState.isAnswerSelected) Modifier.clickable { onAnswerSelected(index) }
                        else Modifier
                    ),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor)
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }

        if (quizState.isAnswerSelected) {
            Button(
                onClick = onNextQuestion,
                modifier = Modifier
                    .padding(top = 24.dp)
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
            modifier = Modifier.padding(top = 24.dp)
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