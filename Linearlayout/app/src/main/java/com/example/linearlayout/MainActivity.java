package com.example.linearlayout;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edtA, edtB;
    Button btnAdd, btnSubtract, btnMultiply, btnDivide;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        edtA = findViewById(R.id.editTextA);
        edtB = findViewById(R.id.editTextB);
        listView = findViewById(R.id.listViewHistory);
        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSub);
        btnMultiply = findViewById(R.id.btnMul);
        btnDivide = findViewById(R.id.btnDiv);

        // Khởi tạo adapter và list
        history = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, history);
        listView.setAdapter(adapter);

        // Sự kiện các nút
        btnAdd.setOnClickListener(view -> calculate("+"));
        btnSubtract.setOnClickListener(view -> calculate("-"));
        btnMultiply.setOnClickListener(view -> calculate("*"));
        btnDivide.setOnClickListener(view -> calculate("/"));
    }

    private void calculate(String operator) {
        String aText = edtA.getText().toString().trim();
        String bText = edtB.getText().toString().trim();

        if (aText.isEmpty() || bText.isEmpty()) {
            Toast.makeText(this, "Please enter both numbers", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    result = a / b;
                    break;
            }

            expression = a + " " + operator + " " + b + " = " + result;
            history.add(0, expression); // Thêm vào đầu danh sách
            adapter.notifyDataSetChanged();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }
}
