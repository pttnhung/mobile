package com.example.ex1;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



public class MainActivity extends AppCompatActivity {
    private TextView tvNumber;
    private FloatingActionButton btnAdd, btnSubtract;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNumber = findViewById(R.id.tv_number);
        btnAdd = findViewById(R.id.btn_add);
        btnSubtract = findViewById(R.id.btn_subtract);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(tvNumber.getText().toString());
                tvNumber.setText("" + ++number);
            }
        });
        btnSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(tvNumber.getText().toString());
                tvNumber.setText("" + --number);
            }
        });

        btnAdd.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                int number = Integer.parseInt(tvNumber.getText().toString());
                tvNumber.setText("" + (number + 5));
                return true;
            }
        });
        btnSubtract.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                int number = Integer.parseInt(tvNumber.getText().toString());
                tvNumber.setText("" + (number-5));
                return true;
            }
        });
    }
}