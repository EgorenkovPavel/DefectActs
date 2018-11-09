package ru.a7flowers.pegorenkov.defectacts.data.network;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBudgeonAmountEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBulkEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueDiameterEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueLengthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueWeigthEntity;

public class Diff {

    //TODO add embedded field good

    private String id;

    private String series;

    private String title;

    private String suplier;

    private String country;

    private String comment;

    private int quantity;

    private int photoQuantity;

    private String deliveryId;

    private String deliveryNumber;

    private int deliveryQuantity;

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

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPhotoQuantity(int photoQuantity) {
        this.photoQuantity = photoQuantity;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWeigth(int weigth) {
        this.weigth = weigth;
    }

    public void setBudgeonAmount(int budgeonAmount) {
        this.budgeonAmount = budgeonAmount;
    }

    public List<Float> getListDiameter() {
        return listDiameter;
    }

    public void setListDiameter(List<Float> listDiameter) {
        this.listDiameter = listDiameter;
    }

    public List<Integer> getListLength() {
        return listLength;
    }

    public void setListLength(List<Integer> listLength) {
        this.listLength = listLength;
    }

    public List<Integer> getListWeigth() {
        return listWeigth;
    }

    public void setListWeigth(List<Integer> listWeigth) {
        this.listWeigth = listWeigth;
    }

    public List<Integer> getListBudgeonAmount() {
        return listBudgeonAmount;
    }

    public void setListBudgeonAmount(List<Integer> listBudgeonAmount) {
        this.listBudgeonAmount = listBudgeonAmount;
    }

    public List<Float> getListBulk() {
        return listBulk;
    }

    public void setListBulk(List<Float> listBulk) {
        this.listBulk = listBulk;
    }

    public int getDeliveryQuantity() {
        return deliveryQuantity;
    }

    public void setDeliveryQuantity(int deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }
}
