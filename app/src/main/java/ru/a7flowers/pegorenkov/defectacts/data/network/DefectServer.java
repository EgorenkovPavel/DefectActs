package ru.a7flowers.pegorenkov.defectacts.data.network;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class DefectServer {

    //TODO add relation

    private String id;
    private String deliveryId;
    private String series;
    private int quantity;
    private int photoQuantity;
    private String comment;

    private List<Reason> reasons;

    public DefectServer(String id, String deliveryId, String series, int quantity, int photoQuantity, String comment, List<Reason> reasons) {
        this.id = id;
        this.deliveryId = deliveryId;
        this.series = series;
        this.quantity = quantity;
        this.photoQuantity = photoQuantity;
        this.comment = comment;
        this.reasons = reasons;
    }

    public DefectServer(Defect defect, List<Reason> reasons) {
        this.id = defect.getId();
        this.deliveryId = defect.getDeliveryId();
        this.series = defect.getSeries();
        this.quantity = defect.getQuantity();
        this.photoQuantity = defect.getPhotoQuantity();
        this.comment = defect.getComment();
        this.reasons = reasons;
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

    public List<Reason> getReasons() {
        return reasons;
    }
}
