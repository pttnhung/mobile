package com.example.listview;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    private EditText etNumber;
    private Button btnSubmit;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        etNumber = findViewById(R.id.et_number);
        btnSubmit = findViewById(R.id.btn_submit);

        int number = getIntent().getIntExtra("number", 0);
        position = getIntent().getIntExtra("position", -1);

        etNumber.setText(String.valueOf(number));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newValueStr = etNumber.getText().toString().trim();
                if (newValueStr.isEmpty()) {
                    Toast.makeText(DetailsActivity.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                    return;
                }

                int newValue;
                try {
                    newValue = Integer.parseInt(newValueStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(DetailsActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position);
                resultIntent.putExtra("new_number", newValue);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
