package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(tableName = "bulk", primaryKeys = {"series", "value"})
public class ValueBulkEntity {

    @NonNull
    @ColumnInfo(name = "series")
    private String series;
    @ColumnInfo(name = "value")
    private int value;

    public ValueBulkEntity(String series, int value) {
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