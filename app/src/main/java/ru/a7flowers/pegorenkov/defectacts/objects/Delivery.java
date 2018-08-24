package ru.a7flowers.pegorenkov.defectacts.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Delivery {
    private String id;
    private String number;
    private Date date;

    private List<Good> goods = new ArrayList<>();
    private DefectAct defectAct;

    public Delivery(String id, String number, Date date) {
        this.id = id;
        this.number = number;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public Date getDate() {
        return date;
    }

    public boolean isActExist() {
        return defectAct != null;
    }

    public String getId() {
        return id;
    }

    public DefectAct getDefectAct() {
        return defectAct;
    }

    public void setDefectAct(DefectAct defectAct) {
        this.defectAct = defectAct;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }
}
