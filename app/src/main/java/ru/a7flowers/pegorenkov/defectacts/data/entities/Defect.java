package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "defects",
foreignKeys = {@ForeignKey(entity = Good.class, parentColumns = "id", childColumns = "goodId"),
        @ForeignKey(entity = DefectAct.class, parentColumns = "id", childColumns = "defectActId")},
indices = {@Index("goodId"), @Index("defectActId")})
public class Defect {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "key")
    private String key;
    @ColumnInfo(name = "goodId")
    private int goodId;
    @ColumnInfo(name = "comment")
    private String comment = "";
    @ColumnInfo(name = "quantity")
    private int quantity;
    @ColumnInfo(name = "photoQuantity")
    private int photoQuantity;
    @ColumnInfo(name = "defectActId")
    private int defectActId;

    public Defect(int id, String key, int goodId, int quantity, int photoQuantity, int defectActId, String comment) {
        this.id = id;
        this.key = key;
        this.goodId = goodId;
        this.quantity = quantity;
        this.photoQuantity = photoQuantity;
        this.defectActId = defectActId;
        this.comment = comment;
    }

    @Ignore
    public Defect(){};


    public String getKey() {
        return key;
    }

    public String getComment() {
        return comment;
    }

    public int getGood() {
        return goodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPhotoQuantity() {
        return photoQuantity;
    }

    public int getId() {
        return id;
    }

    public int getGoodId() {
        return goodId;
    }

    public int getDefectActId() {
        return defectActId;
    }
}
