package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "deliveries")
public class Delivery implements Serializable{

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "number")
    @SerializedName("number")
    private String number;

    @ColumnInfo(name = "date")
    @SerializedName("date")
    private Date date;

    @ColumnInfo(name = "actExist")
    @SerializedName("actExist")
    private boolean actExist;

    public Delivery(String id, String number, Date date, boolean actExist) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.actExist = actExist;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public boolean isActExist() {
        return actExist;
    }

}
