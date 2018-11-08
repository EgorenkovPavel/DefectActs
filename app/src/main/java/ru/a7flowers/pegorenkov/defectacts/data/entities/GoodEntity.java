package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "goods",
primaryKeys = {"series", "deliveryId"},
foreignKeys = {@ForeignKey(entity = Delivery.class, parentColumns = "id", childColumns = "deliveryId", onDelete = ForeignKey.CASCADE)},
indices = {@Index("deliveryId"),@Index("series")})
public class GoodEntity {

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

    @ColumnInfo(name = "deliveryNumber")
    @SerializedName("deliveryNumber")
    private String deliveryNumber;

    @ColumnInfo(name = "diameter")
    @SerializedName("diameter")
    private float diameter;

    @ColumnInfo(name = "length")
    @SerializedName("length")
    private int length;

    @ColumnInfo(name = "weigth")
    @SerializedName("weigth")
    private int weigth;

    @ColumnInfo(name = "budgeonAmount")
    @SerializedName("budgeonAmount")
    private int budgeonAmount;

    @ColumnInfo(name = "bulk")
    @SerializedName("bulk")
    private float bulk;


    public GoodEntity(String series, String good, String suplier, String country, int deliveryQuantity,
                      String deliveryId, String deliveryNumber, float diameter, int length, int weigth,
                      int budgeonAmount, float bulk) {
        this.series = series;
        this.good = good;
        this.suplier = suplier;
        this.country = country;
        this.deliveryQuantity = deliveryQuantity;
        this.deliveryId = deliveryId;
        this.deliveryNumber = deliveryNumber;

        this.diameter = diameter;
        this.length = length;
        this.weigth = weigth;
        this.budgeonAmount = budgeonAmount;
        this.bulk = bulk;
    }

    @Ignore
    public GoodEntity(){}

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

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setSeries(@NonNull String series) {
        this.series = series;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public void setSuplier(String suplier) {
        this.suplier = suplier;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDeliveryQuantity(@NonNull int deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    public void setDeliveryId(@NonNull String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public float getDiameter() {
        return diameter;
    }

    public int getLength() {
        return length;
    }

    public int getWeigth() {
        return weigth;
    }

    public int getBudgeonAmount() {
        return budgeonAmount;
    }

    public float getBulk() {
        return bulk;
    }
}
