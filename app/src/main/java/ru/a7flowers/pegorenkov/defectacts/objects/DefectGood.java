package ru.a7flowers.pegorenkov.defectacts.objects;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;

public class DefectGood {

    private int id;
    private String key;
    private Good good;
    private String comment = "";
    private int quantity;
    private int photoQuantity;
    private int defectActId;

    public int getId() {
        return id;
    }

    public Good getGood() {
        return good;
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
}
