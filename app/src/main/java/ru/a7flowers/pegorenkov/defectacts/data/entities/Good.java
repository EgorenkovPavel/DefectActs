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
    private String suplier;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "deliveryQuantity")
    private int deliveryQuantity;
    @ColumnInfo(name = "deliveryId")
    private int deliveryId;

    public Good(int id, String series, String good, String suplier, String country, int deliveryQuantity, int deliveryId) {
        this.id = id;
        this.series = series;
        this.good = good;
        this.suplier = suplier;
        this.country = country;
        this.deliveryQuantity = deliveryQuantity;
        this.deliveryId = deliveryId;
    }

    public String getSeries() {
        return series;
    }

    public String getGood() {
        return good;
    }

    public String getSuplier() {
        return suplier;
    }

    public String getCountry() {
        return country;
    }

    public int getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public int getId() {
        return id;
    }

    public int getDeliveryId() {
        return deliveryId;
    }
}
