package com.example.linearlayout;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText edtA, edtB;
    private Button btnAdd, btnSubtract, btnMultiply, btnDivide, btnClear;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupAdapter();
        setupClickListeners();
    }

    private void initializeViews() {
        edtA = findViewById(R.id.editTextA);
        edtB = findViewById(R.id.editTextB);
        listView = findViewById(R.id.listViewHistory);
        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSub);
        btnMultiply = findViewById(R.id.btnMul);
        btnDivide = findViewById(R.id.btnDiv);
        btnClear = findViewById(R.id.btnClear);
    }

    private void setupAdapter() {
        history = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, history);
        if (listView != null) {
            listView.setAdapter(adapter);
        }
    }

    private void setupClickListeners() {
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> calculate("+"));
        }
        if (btnSubtract != null) {
            btnSubtract.setOnClickListener(v -> calculate("-"));
        }
        if (btnMultiply != null) {
            btnMultiply.setOnClickListener(v -> calculate("*"));
        }
        if (btnDivide != null) {
            btnDivide.setOnClickListener(v -> calculate("/"));
        }
        if (btnClear != null) {
            btnClear.setOnClickListener(v -> clearAll());
        }
    }

    private void calculate(String operator) {
        if (edtA == null || edtB == null) {
            return;
        }

        String aText = edtA.getText().toString().trim();
        String bText = edtB.getText().toString().trim();

        if (aText.isEmpty() || bText.isEmpty()) {
            showToast("Please enter both numbers");
            return;
        }

        try {
            double a = Double.parseDouble(aText);
            double b = Double.parseDouble(bText);
            double result = 0;
            String expression;

            switch (operator) {
                case "+":
                    result = a + b;
                    break;
                case "-":
                    result = a - b;
                    break;
                case "*":
                    result = a * b;
                    break;
                case "/":
                    if (b == 0) {
                        showToast("Cannot divide by zero");
                        return;
                    }
                    result = a / b;
                    break;
                default:
                    return;
            }

            expression = String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
            addToHistory(expression);

        } catch (NumberFormatException e) {
            showToast("Invalid number format");
        } catch (Exception e) {
            showToast("Calculation error");
        }
    }

    private void addToHistory(String expression) {
        if (history != null && adapter != null) {
            history.add(0, expression);
            adapter.notifyDataSetChanged();
        }
    }

    private void clearAll() {
        if (history != null) {
            history.clear();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        
        if (edtA != null) {
            edtA.setText("");
        }
        if (edtB != null) {
            edtB.setText("");
        }
        
        if (edtA != null) {
            edtA.requestFocus();
        }
        
        showToast("All cleared");
    }

    private void showToast(String message) {
        if (!isFinishing()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
