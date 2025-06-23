package com.example.mdd_contactapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM Contact ORDER BY name")
    List<Contact> getAll();

    @Insert
    void insert(Contact...contacts);

    @Delete
    void delete(Contact contact);
}
