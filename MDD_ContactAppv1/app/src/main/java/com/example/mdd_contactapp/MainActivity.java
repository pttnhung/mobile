package com.example.mdd_contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mdd_contactapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.OnContactDeleteListener {

    private ActivityMainBinding binding;

    private ArrayList<Contact> contactList;
    private ArrayList<Contact> filteredList;
    private ContactsAdapter contactsAdapter;

    private AppDatabase appDatabase;
    private ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up RecyclerView and Adapter
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();
        filteredList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(filteredList);
        contactsAdapter.setOnContactDeleteListener(this);
        binding.rvContacts.setAdapter(contactsAdapter);

        // Initialize database
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        contactDao = appDatabase.contactDao();

        // Load contacts from the database
        loadContactsFromDatabase();

        // Add a new contact button (Floating Action Button)
        binding.btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewContactActivity.class);
            startActivity(intent);
        });

        // SearchView listener
        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterContacts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText);
                return true;
            }
        });
    }

    // Method to load contacts from the database
    private void loadContactsFromDatabase() {
        new Thread(() -> {
            List<Contact> contacts = contactDao.getAll();  // Lấy danh bạ từ database
            runOnUiThread(() -> {
                if (contacts != null && !contacts.isEmpty()) {
                    contactList.clear();
                    contactList.addAll(contacts);

                    filteredList.clear();
                    filteredList.addAll(contactList);

                    contactsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "No contacts found.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    // Filter contacts based on search query
    private void filterContacts(String query) {
        filteredList.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(contactList); // Show all if empty
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Contact contact : contactList) {
                if (contact.getName().toLowerCase().contains(lowerCaseQuery)
                        || contact.getPhone().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(contact);
                }
            }
        }
        contactsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onContactDelete(Contact contact, int position) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete " + contact.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete from database
                    new Thread(() -> {
                        contactDao.delete(contact);
                        runOnUiThread(() -> {
                            // Remove from lists
                            contactList.remove(contact);
                            filteredList.remove(contact);
                            
                            // Update adapter
                            contactsAdapter.notifyDataSetChanged();
                            
                            Toast.makeText(this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                        });
                    }).start();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContactsFromDatabase();  // Reload contacts on returning to MainActivity
    }
}
