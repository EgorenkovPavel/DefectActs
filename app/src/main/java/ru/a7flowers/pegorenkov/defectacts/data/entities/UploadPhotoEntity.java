package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "uploadPhotos")
public class UploadPhotoEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "delivery_id")
    private String deliveryId;

    @ColumnInfo(name = "defect_id")
    private String defectId;

    @ColumnInfo(name = "diff_id")
    private String diffId;

    @ColumnInfo(name = "photo_path")
    private String photoPath;

    @ColumnInfo(name = "try_number")
    private int tryNumber;

    public UploadPhotoEntity(int id, String userId, String deliveryId, String defectId, String diffId, String photoPath, int tryNumber) {
        this.id = id;
        this.userId = userId;
        this.deliveryId = deliveryId;
        this.defectId = defectId;
        this.diffId = diffId;
        this.photoPath = photoPath;
        this.tryNumber = tryNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getDefectId() {
        return defectId;
    }

    public String getDiffId() {
        return diffId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public int getTryNumber() {
        return tryNumber;
    }

    public int getId() {
        return id;
    }
}
