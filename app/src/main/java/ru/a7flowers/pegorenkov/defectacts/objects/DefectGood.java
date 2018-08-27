package ru.a7flowers.pegorenkov.defectacts.objects;

import android.arch.persistence.room.Embedded;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;

public class DefectGood {

    private int id;
    private String key;
    private int goodId;

    private String series;
    private String good;
    private String suplier;
    private String country;
    private int deliveryQuantity;

    private String comment = "";
    private int quantity;
    private int photoQuantity;
    private int deliveryId;

    public DefectGood(int id, String key, String series, String good, int goodId, String suplier, String country, int deliveryQuantity, String comment, int quantity, int photoQuantity, int deliveryId) {
        this.id = id;
        this.key = key;
        this.series = series;
        this.goodId = goodId;
        this.good = good;
        this.suplier = suplier;
        this.country = country;
        this.deliveryQuantity = deliveryQuantity;
        this.comment = comment;
        this.quantity = quantity;
        this.photoQuantity = photoQuantity;
        this.deliveryId = deliveryId;
    }

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPhotoQuantity() {
        return photoQuantity;
    }

    public String getKey() {
        return key;
    }

    public String getSeries() {
        return series;
    }

    public int getGoodId() {
        return goodId;
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

    public int getDeliveryId() {
        return deliveryId;
    }
}
