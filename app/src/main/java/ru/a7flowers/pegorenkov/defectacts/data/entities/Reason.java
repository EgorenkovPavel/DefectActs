package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "reasons")
public class Reason implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "key")
    private String key;
    @ColumnInfo(name = "title")
    private String title;

    public Reason(int id, String key, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }
}
