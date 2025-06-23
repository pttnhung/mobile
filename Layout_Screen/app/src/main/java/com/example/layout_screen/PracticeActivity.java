package com.example.layout_screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PracticeActivity extends AppCompatActivity {
    private TextView tvProgress, tvQuestion;
    private Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnNext;
    private int currentQuestion = 0;
    private int correctCount = 0;
    private int selectedAnswer = -1;

    // Demo dữ liệu quiz
    private String[] questions = {
            "What is the capital of France?",
            "Which animal barks?",
            "What color is the sky?",
            "How many days in a week?",
            "Which fruit is yellow?"
    };
    private String[][] answers = {
            {"London", "Paris", "Berlin", "Rome"},
            {"Cat", "Dog", "Fish", "Bird"},
            {"Blue", "Red", "Green", "Black"},
            {"5", "6", "7", "8"},
            {"Apple", "Banana", "Grape", "Orange"}
    };
    private int[] correctIndexes = {1, 1, 0, 2, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        tvProgress = findViewById(R.id.tvProgress);
        tvQuestion = findViewById(R.id.tvQuestion);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
        btnNext = findViewById(R.id.btnNext);

        setupAnswerButton(btnAnswer1, 0);
        setupAnswerButton(btnAnswer2, 1);
        setupAnswerButton(btnAnswer3, 2);
        setupAnswerButton(btnAnswer4, 3);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAnswer == correctIndexes[currentQuestion]) correctCount++;
                currentQuestion++;
                if (currentQuestion < questions.length) {
                    loadQuestion();
                } else {
                    Intent intent = new Intent(PracticeActivity.this, ResultActivity.class);
                    intent.putExtra("score", correctCount);
                    intent.putExtra("total", questions.length);
                    startActivity(intent);
                    finish();
                }
            }
        });
        loadQuestion();
    }

    private void setupAnswerButton(final Button btn, final int index) {
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (selectedAnswer != index) {
                        btn.setBackgroundColor(Color.parseColor("#FFB74D")); // Cam nhạt khi nhấn
                        btn.setTextColor(Color.WHITE);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    if (selectedAnswer != index) {
                        btn.setBackgroundColor(Color.parseColor("#64B5F6")); // Xanh dương nhạt mặc định
                        btn.setTextColor(Color.BLACK);
                    }
                }
                return false; // Cho phép onClick tiếp tục chạy
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedAnswer = index;
                updateAnswerButtonColors();
            }
        });
    }

    private void updateAnswerButtonColors() {
        for (int i = 0; i < 4; i++) {
            Button btn = getButtonByIndex(i);
            if (selectedAnswer == i) {
                btn.setBackgroundColor(Color.parseColor("#FFA726")); // Cam đậm khi chọn
                btn.setTextColor(Color.WHITE);
            } else {
                btn.setBackgroundColor(Color.parseColor("#64B5F6")); // Xanh dương nhạt mặc định
                btn.setTextColor(Color.BLACK);
            }
        }
    }

    private Button getButtonByIndex(int i) {
        switch (i) {
            case 0: return btnAnswer1;
            case 1: return btnAnswer2;
            case 2: return btnAnswer3;
            case 3: return btnAnswer4;
        }
        return null;
    }

    private void loadQuestion() {
        tvProgress.setText((currentQuestion+1) + "/" + questions.length);
        tvQuestion.setText(questions[currentQuestion]);
        btnAnswer1.setText(answers[currentQuestion][0]);
        btnAnswer2.setText(answers[currentQuestion][1]);
        btnAnswer3.setText(answers[currentQuestion][2]);
        btnAnswer4.setText(answers[currentQuestion][3]);
        selectedAnswer = -1;
        updateAnswerButtonColors();
    }
} 