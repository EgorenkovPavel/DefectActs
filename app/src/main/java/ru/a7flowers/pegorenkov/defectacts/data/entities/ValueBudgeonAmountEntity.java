package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(tableName = "budgeonAmount", primaryKeys = {"series", "value"})
public class ValueBudgeonAmountEntity {

    @NonNull
    @ColumnInfo(name = "series")
    private String series;
    @ColumnInfo(name = "value")
    private int value;

    public ValueBudgeonAmountEntity(String series, int value) {
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