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

    private float diameter;

    private int length;

    private int weigth;

    private int budgeonAmount;

    private float bulk;

    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueDiameterEntity.class, projection = {"value"})
    private List<Float> listDiameter;
    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueLengthEntity.class, projection = {"value"})
    private List<Integer> listLength;
    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueWeigthEntity.class, projection = {"value"})
    private List<Integer> listWeigth;
    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueBudgeonAmountEntity.class, projection = {"value"})
    private List<Integer> listBudgeonAmount;
    @Relation(parentColumn = "series", entityColumn = "series", entity = ValueBulkEntity.class, projection = {"value"})
    private List<Float> listBulk;

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

    public List<Float> getListDiameter() {
        return listDiameter;
    }

    public void setListDiameter(List<Float> diameter) {
        this.listDiameter = diameter;
    }

    public List<Integer> getListLength() {
        return listLength;
    }

    public void setListLength(List<Integer> length) {
        this.listLength = length;
    }

    public List<Integer> getListWeigth() {
        return listWeigth;
    }

    public void setListWeigth(List<Integer> weigth) {
        this.listWeigth = weigth;
    }

    public List<Integer> getListBudgeonAmount() {
        return listBudgeonAmount;
    }

    public void setListBudgeonAmount(List<Integer> budgeonAmount) {
        this.listBudgeonAmount = budgeonAmount;
    }

    public List<Float> getListBulk() {
        return listBulk;
    }

    public void setListBulk(List<Float> bulk) {
        this.listBulk = bulk;
    }

    public float getDiameter() {
        return diameter;
    }

    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWeigth() {
        return weigth;
    }

    public void setWeigth(int weigth) {
        this.weigth = weigth;
    }

    public int getBudgeonAmount() {
        return budgeonAmount;
    }

    public void setBudgeonAmount(int budgeonAmount) {
        this.budgeonAmount = budgeonAmount;
    }

    public float getBulk() {
        return bulk;
    }

    public void setBulk(float bulk) {
        this.bulk = bulk;
    }
}
