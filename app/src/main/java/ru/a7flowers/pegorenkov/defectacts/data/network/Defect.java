package ru.a7flowers.pegorenkov.defectacts.data.network;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReasonEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class Defect {

//    @Embedded
//    private DefectEntity mDefect;

    @SerializedName("id")
    private String id;

    @SerializedName("series")
    private String series;

    @SerializedName("comment")
    private String comment = "";

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("writeoff")
    private int writeoff;

    @SerializedName("photoQuantity")
    private int photoQuantity;

    @SerializedName("deliveryId")
    private String deliveryId;

    @SerializedName("deliveryNumber")
    private String deliveryNumber;

    private int deliveryQuantity;

    @SerializedName("title")
    private String title;

    @SerializedName("suplier")
    private String suplier;

    @SerializedName("country")
    private String country;

    @SerializedName("reasons")
    @Relation(parentColumn = "id", entityColumn = "defectId", entity = DefectReasonEntity.class)
    private List<DefectReasonEntity> mReasons = new ArrayList<>();

    public Defect() {
    }

    @Ignore
    public Defect(DefectEntity defect, List<Reason> reasons) {
//        mDefect = defect;
        setDefect(defect);

        mReasons = new ArrayList<>();
        for (Reason reason:reasons) {
            mReasons.add(new DefectReasonEntity(defect.getId(), reason.getId(), reason.getTitle()));
        }
    }

    public List<DefectReasonEntity> getReasons() {
        for (DefectReasonEntity reason:mReasons) {
            reason.setDefectId(id);
        }
        return mReasons;
    }

    public void setReasons(List<DefectReasonEntity> reasons) {
        mReasons = reasons;
    }

    public DefectEntity getDefect() {
        return new DefectEntity(id, series, quantity, writeoff, photoQuantity, deliveryId, comment);
    }

    public void setDefect(DefectEntity defect) {
        this.id = defect.getId();
        this.deliveryId = defect.getDeliveryId();
        this.series = defect.getSeries();
        this.comment = defect.getComment();
        this.quantity = defect.getQuantity();
        this.writeoff = defect.getWriteoff();
        this.photoQuantity = defect.getPhotoQuantity();
    }

    public String getId() {
        return id;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public int getPhotoQuantity() {
        return photoQuantity;
    }

    public String getSeries() {
        return series;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPhotoQuantity(int photoQuantity) {
        this.photoQuantity = photoQuantity;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getSuplier() {
        return suplier;
    }

    public String getCountry() {
        return country;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSuplier(String suplier) {
        this.suplier = suplier;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public Integer getWriteoff() {
        return writeoff;
    }

    public void setWriteoff(int writeoff) {
        this.writeoff = writeoff;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public int getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(int deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }
}
