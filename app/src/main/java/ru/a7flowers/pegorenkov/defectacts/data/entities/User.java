package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "users")
public class User{

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "defectAccess")
    @SerializedName("defectAccess")
    private boolean defectAccess;

    @ColumnInfo(name = "diffAccess")
    @SerializedName("diffAccess")
    private boolean diffAccess;

    public User(@NonNull String id, String name, boolean defectAccess, boolean diffAccess) {
        this.id = id;
        this.name = name;
        this.defectAccess = defectAccess;
        this.diffAccess = diffAccess;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isDefectAccess() {
        return defectAccess;
    }

    public boolean isDiffAccess() {
        return diffAccess;
    }
}
