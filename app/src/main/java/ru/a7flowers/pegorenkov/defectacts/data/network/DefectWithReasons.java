package ru.a7flowers.pegorenkov.defectacts.data.network;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class DefectWithReasons{

//    @Embedded
//    private Defect mDefect;

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

    @SerializedName("title")
    private String title;

    @SerializedName("suplier")
    private String suplier;

    @SerializedName("country")
    private String country;

    @SerializedName("reasons")
    @Relation(parentColumn = "id", entityColumn = "defectId", entity = DefectReason.class)
    private List<DefectReason> mReasons;

    public DefectWithReasons() {
    }

    @Ignore
    public DefectWithReasons(Defect defect, List<Reason> reasons) {
//        mDefect = defect;
        setDefect(defect);

        mReasons = new ArrayList<>();
        for (Reason reason:reasons) {
            mReasons.add(new DefectReason(defect.getId(), reason.getId()));
        }
    }

    public List<DefectReason> getReasons() {
        return mReasons;
    }

    public void setReasons(List<DefectReason> reasons) {
        mReasons = reasons;
    }

    public Defect getDefect() {
        return new Defect(id, series, quantity, writeoff, photoQuantity, deliveryId, comment);
    }

    public void setDefect(Defect defect) {
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
}
