package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(tableName = "diameter", primaryKeys = {"series", "value"})
public class ValueDiameterEntity {

    @NonNull
    @ColumnInfo(name = "series")
    private String series;
    @ColumnInfo(name = "value")
    private int value;

    public ValueDiameterEntity(String series, int value) {
        this.series = series;
        this.value = value;
    }

    public String getSeries() {
        return series;
    }

    public int getValue() {
        return value;
    }
}
