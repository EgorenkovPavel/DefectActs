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

import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

@Entity(tableName = "defects",
foreignKeys = {@ForeignKey(entity = Delivery.class, parentColumns = "id", childColumns = "deliveryId")},
indices = {@Index("deliveryId"), @Index("series")})
public class Defect implements Serializable{

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

    public Defect(String id, String series, int quantity, int photoQuantity, String deliveryId, String comment) {
        this.id = id;
        this.series = series;
        this.quantity = quantity;
        this.photoQuantity = photoQuantity;
        this.deliveryId = deliveryId;
        this.comment = comment;
    }

    @Ignore
    public Defect(){}

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

    @Ignore
    public Defect(DefectGood defectGood) {
        this.id = defectGood.getId();
        this.series = defectGood.getSeries();
        this.quantity = defectGood.getQuantity();
        this.photoQuantity = defectGood.getPhotoQuantity();
        this.deliveryId = defectGood.getDeliveryId();
        this.comment = defectGood.getComment();
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
