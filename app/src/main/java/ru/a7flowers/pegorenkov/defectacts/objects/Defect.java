package ru.a7flowers.pegorenkov.defectacts.objects;

import java.util.ArrayList;
import java.util.List;

public class Defect {

    private String key;
    private Good good;
    private String comment = "";
    private int quantity;
    private int photoQuantity;

    private List<Reason> reasons = new ArrayList<>();

    public Defect(){};

    public Defect(String key, Good good, int quantity, int photoQuantity) {
        this.key = key;
        this.good = good;
        this.quantity = quantity;
        this.photoQuantity = photoQuantity;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public List<Reason> getReasons() {
        return reasons;
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
    }

    public String getKey() {
        return key;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Good getGood() {
        return good;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPhotoQuantity() {
        return photoQuantity;
    }
}
