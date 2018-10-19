package ru.a7flowers.pegorenkov.defectacts.data.network;

import android.arch.persistence.room.Relation;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBudgeonAmountEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBulkEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueDiameterEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueLengthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueWeigthEntity;

public class Good {

    private String series;

    private String good;

    private String suplier;

    private String country;

    private int deliveryQuantity;

    private String deliveryId;

    private String deliveryNumber;

    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueDiameterEntity.class, projection = {"value"})
    private List<Integer> diameter;
    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueLengthEntity.class, projection = {"value"})
    private List<Integer> length;
    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueWeigthEntity.class, projection = {"value"})
    private List<Integer> weigth;
    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueBudgeonAmountEntity.class, projection = {"value"})
    private List<Integer> budgeonAmount;
    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueBulkEntity.class, projection = {"value"})
    private List<Integer> bulk;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getSuplier() {
        return suplier;
    }

    public void setSuplier(String suplier) {
        this.suplier = suplier;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(int deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public List<Integer> getDiameter() {
        return diameter;
    }

    public void setDiameter(List<Integer> diameter) {
        this.diameter = diameter;
    }

    public List<Integer> getLength() {
        return length;
    }

    public void setLength(List<Integer> length) {
        this.length = length;
    }

    public List<Integer> getWeigth() {
        return weigth;
    }

    public void setWeigth(List<Integer> weigth) {
        this.weigth = weigth;
    }

    public List<Integer> getBudgeonAmount() {
        return budgeonAmount;
    }

    public void setBudgeonAmount(List<Integer> budgeonAmount) {
        this.budgeonAmount = budgeonAmount;
    }

    public List<Integer> getBulk() {
        return bulk;
    }

    public void setBulk(List<Integer> bulk) {
        this.bulk = bulk;
    }
}
