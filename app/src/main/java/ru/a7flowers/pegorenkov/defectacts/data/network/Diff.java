package ru.a7flowers.pegorenkov.defectacts.data.network;

import android.arch.persistence.room.Ignore;

public class Diff {

    private String id;

    private String series;

    private String title;

    private String suplier;

    private String country;

    private String comment;

    private int quantity;

    private int photoQuantity;

    private String deliveryId;

    private float diameter;

    private int length;

    private int weigth;

    private int budgeonAmount;

    private float bulk;

    public Diff(String id, String series, String title, String suplier, String country,
                String comment, int quantity, int photoQuantity, String deliveryId,
                float diameter, int length, int weigth, int budgeonAmount, float bulk) {
        this.id = id;
        this.series = series;
        this.title = title;
        this.suplier = suplier;
        this.country = country;
        this.comment = comment;
        this.quantity = quantity;
        this.photoQuantity = photoQuantity;
        this.deliveryId = deliveryId;
        this.diameter = diameter;
        this.length = length;
        this.weigth = weigth;
        this.budgeonAmount = budgeonAmount;
        this.bulk = bulk;
    }

    @Ignore
    public Diff() {
    }

    public String getId() {
        return id;
    }

    public String getSeries() {
        return series;
    }

    public String getTitle() {
        return title;
    }

    public String getSuplier() {
        return suplier;
    }

    public String getCountry() {
        return country;
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

    public String getDeliveryId() {
        return deliveryId;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setQuantity(Integer quantity) {
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

    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setWeigth(Integer weigth) {
        this.weigth = weigth;
    }

    public void setBudgeonAmount(Integer budgeonAmount) {
        this.budgeonAmount = budgeonAmount;
    }

    public void setBulk(float bulk) {
        this.bulk = bulk;
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
}
