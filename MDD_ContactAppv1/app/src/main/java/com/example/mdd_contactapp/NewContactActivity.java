package com.example.mdd_contactapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mdd_contactapp.databinding.ActivityNewContactBinding;

public class NewContactActivity extends AppCompatActivity {

    private ActivityNewContactBinding binding;

    private AppDatabase appDatabase;
    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Room database
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        contactDao = appDatabase.contactDao();

        // Handle save button click
        binding.btnSave.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Contact contact = new Contact(name, phone, email);

            // Save contact using AsyncTask
            AsyncTask.execute(() -> {
                contactDao.insert(contact);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Contact saved!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity
                });
            });
        });
    }
}
