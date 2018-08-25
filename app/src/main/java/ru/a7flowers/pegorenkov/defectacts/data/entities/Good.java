package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "goods",
foreignKeys = {@ForeignKey(entity = Delivery.class, parentColumns = "id", childColumns = "deliveryId")},
indices = {@Index("deliveryId")})
public class Good {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "series")
    private String series;
    @ColumnInfo(name = "good")
    private String good;
    @ColumnInfo(name = "suplier")
    private String supier;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "quantity")
    private int quantity;
    @ColumnInfo(name = "deliveryId")
    private int deliveryId;

    public Good(int id, String series, String good, String supier, String country, int quantity, int deliveryId) {
        this.id = id;
        this.series = series;
        this.good = good;
        this.supier = supier;
        this.country = country;
        this.quantity = quantity;
        this.deliveryId = deliveryId;
    }

    public String getSeries() {
        return series;
    }

    public String getGood() {
        return good;
    }

    public String getSupier() {
        return supier;
    }

    public String getCountry() {
        return country;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }

    public int getDeliveryId() {
        return deliveryId;
    }
}
