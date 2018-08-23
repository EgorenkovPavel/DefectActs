package ru.a7flowers.pegorenkov.defectacts.objects;

public class Reason {

    private String id;
    private String title;

    public Reason(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
