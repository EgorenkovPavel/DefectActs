package ru.a7flowers.pegorenkov.defectacts.objects;

public class Defect {

    private String id;
    private String series;
    private String good;
    private String supier;
    private String country;
    private int quantity;
    private int photoQuantity;

    public Defect(String id, String series, String good, String supier, String country, int quantity, int photoQuantity) {
        this.id = id;
        this.series = series;
        this.good = good;
        this.supier = supier;
        this.country = country;
        this.quantity = quantity;
        this.photoQuantity = photoQuantity;
    }

    public String getSeries() {
        return series;
    }

    public String getGood() {
        return good;
    }

    public String getSupier() {
        return supier;
    }

    public String getCountry() {
        return country;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPhotoQuantity() {
        return photoQuantity;
    }
}
