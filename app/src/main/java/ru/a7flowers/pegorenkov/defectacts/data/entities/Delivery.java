package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "deliveries")
public class Delivery {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "key")
    private String key;
    @ColumnInfo(name = "number")
    private String number;
    @ColumnInfo(name = "date")
    private Date date;

    public Delivery(int id, String key, String number, Date date) {
        this.id = id;
        this.key = key;
        this.number = number;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public String getKey() {
        return key;
    }

    public boolean isActExist() {
        return false;
    }
}
