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

import ru.a7flowers.pegorenkov.defectacts.data.network.Defect;

@Entity(tableName = "defects",
foreignKeys = {@ForeignKey(entity = Delivery.class, parentColumns = "id", childColumns = "deliveryId", onDelete = ForeignKey.CASCADE)},
indices = {@Index("deliveryId"), @Index("series")})
public class DefectEntity implements Serializable{

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

    @ColumnInfo(name = "writeoff")
    @SerializedName("writeoff")
    private int writeoff;

    @ColumnInfo(name = "photoQuantity")
    @SerializedName("photoQuantity")
    private int photoQuantity;

    @ColumnInfo(name = "deliveryId")
    @SerializedName("deliveryId")
    private String deliveryId;

    public DefectEntity(String id, String series, int quantity, int writeoff, int photoQuantity, String deliveryId, String comment) {
        this.id = id;
        this.series = series;
        this.quantity = quantity;
        this.writeoff = writeoff;
        this.photoQuantity = photoQuantity;
        this.deliveryId = deliveryId;
        this.comment = comment;
    }

    @Ignore
    public DefectEntity(){}

    @Ignore
    public DefectEntity(Defect defectServer) {
        this.id = defectServer.getId();
        this.series = defectServer.getSeries();
        this.quantity = defectServer.getQuantity();
        this.writeoff = defectServer.getWriteoff();
        this.photoQuantity = defectServer.getPhotoQuantity();
        this.deliveryId = defectServer.getDeliveryId();
        this.comment = defectServer.getComment();
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

    public void setWriteoff(Integer writeoff) {
        this.writeoff = writeoff;
    }

    public int getWriteoff() {
        return writeoff;
    }
}
