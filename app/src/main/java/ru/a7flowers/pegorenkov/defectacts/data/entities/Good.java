package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "goods",
primaryKeys = {"series", "deliveryId"},
foreignKeys = {@ForeignKey(entity = Delivery.class, parentColumns = "id", childColumns = "deliveryId", onDelete = ForeignKey.CASCADE)},
indices = {@Index("deliveryId"),@Index("series")})
public class Good {

    @NonNull
    @ColumnInfo(name = "series")
    @SerializedName("series")
    private String series;

    @ColumnInfo(name = "good")
    @SerializedName("good")
    private String good;

    @ColumnInfo(name = "suplier")
    @SerializedName("suplier")
    private String suplier;

    @ColumnInfo(name = "country")
    @SerializedName("country")
    private String country;

    @ColumnInfo(name = "deliveryQuantity")
    @NonNull
    @SerializedName("quantity")
    private int deliveryQuantity;

    @ColumnInfo(name = "deliveryId")
    @SerializedName("deliveryId")
    @NonNull
    private String deliveryId;

    public Good(String series, String good, String suplier, String country, int deliveryQuantity, String deliveryId) {
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

    public String getDeliveryId() {
        return deliveryId;
    }
}
