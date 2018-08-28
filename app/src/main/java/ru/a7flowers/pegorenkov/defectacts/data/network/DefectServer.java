package ru.a7flowers.pegorenkov.defectacts.data.network;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class DefectServer {

    private String id;
    private String series;
    private int quantity;
    private String comment;

    private List<Reason> reasons;

    public DefectServer(String id, String series, int quantity, String comment, List<Reason> reasons) {
        this.id = id;
        this.series = series;
        this.quantity = quantity;
        this.comment = comment;
        this.reasons = reasons;
    }

    public DefectServer(Defect defect, List<Reason> reasons) {
        this.id = defect.getId();
        this.series = defect.getSeries();
        this.quantity = defect.getQuantity();
        this.comment = defect.getComment();
        this.reasons = reasons;
    }

    public String getId() {
        return id;
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
