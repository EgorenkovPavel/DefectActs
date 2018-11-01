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

    @ColumnInfo(name = "defectActExist")
    @SerializedName("defectActExist")
    private boolean defectActExist;

    @ColumnInfo(name = "differenceActExist")
    @SerializedName("differenceActExist")
    private boolean differenceActExist;

    @ColumnInfo(name = "photoCount")
    @SerializedName("photoCount")
    private int photoCount;

    public Delivery(@NonNull String id, String number, Date date, boolean defectActExist,
                    boolean differenceActExist,
                    int photoCount) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.defectActExist = defectActExist;
        this.differenceActExist = differenceActExist;
        this.photoCount = photoCount;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public boolean isDefectActExist() {
        return defectActExist;
    }

    public boolean isDifferenceActExist() {
        return differenceActExist;
    }

    public int getPhotoCount() {
        return photoCount;
    }
}
