package ru.a7flowers.pegorenkov.defectacts.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Reason implements Serializable{

    private String id;
    private String title;

    public Reason(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
