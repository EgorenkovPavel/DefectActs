package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

@Entity(tableName = "defects",
foreignKeys = {@ForeignKey(entity = Delivery.class, parentColumns = "id", childColumns = "deliveryId")},
indices = {@Index("deliveryId"), @Index("series")})
public class Defect implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "key")
    private String key;
    @ColumnInfo(name = "series")
    private String series;
    @ColumnInfo(name = "comment")
    private String comment = "";
    @ColumnInfo(name = "quantity")
    private int quantity;
    @ColumnInfo(name = "photoQuantity")
    private int photoQuantity;
    @ColumnInfo(name = "deliveryId")
    private int deliveryId;

    public Defect(int id, String key, String series, int quantity, int photoQuantity, int deliveryId, String comment) {
        this.id = id;
        this.key = key;
        this.series = series;
        this.quantity = quantity;
        this.photoQuantity = photoQuantity;
        this.deliveryId = deliveryId;
        this.comment = comment;
    }

    @Ignore
    public Defect(){};

    public String getKey() {
        return key;
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

    public int getId() {
        return id;
    }

    public int getDeliveryId() {
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
        this.key = defectGood.getKey();
        this.series = defectGood.getSeries();
        this.quantity = defectGood.getQuantity();
        this.photoQuantity = defectGood.getPhotoQuantity();
        this.deliveryId = defectGood.getDeliveryId();
        this.comment = defectGood.getComment();
    }
}
