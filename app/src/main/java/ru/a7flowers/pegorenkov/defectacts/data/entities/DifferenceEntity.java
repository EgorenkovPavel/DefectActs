package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;

@Entity(tableName = "differencies",
foreignKeys = {@ForeignKey(entity = Delivery.class, parentColumns = "id", childColumns = "deliveryId", onDelete = ForeignKey.CASCADE)},
indices = {@Index("deliveryId"), @Index("series")})
public class DifferenceEntity implements Serializable{

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "series")
    @SerializedName("series")
    private String series;

    @ColumnInfo(name = "comment")
    @SerializedName("comment")
    private String comment = "";

    @ColumnInfo(name = "quantity")
    @SerializedName("quantity")
    private int quantity;

    @ColumnInfo(name = "photoQuantity")
    @SerializedName("photoQuantity")
    private int photoQuantity;

    @ColumnInfo(name = "deliveryId")
    @SerializedName("deliveryId")
    private String deliveryId;

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

    public DifferenceEntity(String id, String series, int quantity, int photoQuantity,
                            String deliveryId, String comment,
                            float diameter, int length, int weigth, int budgeonAmount, float bulk) {
        this.id = id;
        this.series = series;
        this.quantity = quantity;
        this.photoQuantity = photoQuantity;
        this.deliveryId = deliveryId;
        this.comment = comment;
        this.diameter = diameter;
        this.length = length;
        this.weigth = weigth;
        this.budgeonAmount = budgeonAmount;
        this.bulk = bulk;
    }

    @Ignore
    public DifferenceEntity(){}

    @Ignore
    public DifferenceEntity(Diff diff) {
        this.id = diff.getId();
        this.series = diff.getSeries();
        this.quantity = diff.getQuantity();
        this.photoQuantity = diff.getPhotoQuantity();
        this.deliveryId = diff.getDeliveryId();
        this.comment = diff.getComment();
        this.diameter = diff.getDiameter();
        this.length = diff.getLength();
        this.weigth = diff.getWeigth();
        this.budgeonAmount = diff.getBudgeonAmount();
        this.bulk = diff.getBulk();
    }

    public String getComment() {
        return comment;
    }

    public String getSeries() {
        return series;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPhotoQuantity() {
        return photoQuantity;
    }

    public String getId() {
        return id;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setId(@NonNull String id) {
        this.id = id;
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
