package ru.a7flowers.pegorenkov.defectacts.objects;

import java.util.Date;

public class Delivery {
    private String id;
    private String actId;
    private String number;
    private Date date;
    private int photoAmount;
    private boolean actExist;

    public Delivery(String id, String number, Date date, int photoAmount, boolean actExist) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.photoAmount = photoAmount;
        this.actExist = actExist;
    }

    public String getActId() {
        return actId;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public int getPhotoAmount() {
        return photoAmount;
    }

    public boolean isActExist() {
        return actExist;
    }
}
