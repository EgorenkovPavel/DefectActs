package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "defectacts",
        foreignKeys = {@ForeignKey(entity = Delivery.class, parentColumns = "id", childColumns = "deliveryId")},
        indices = {@Index("deliveryId")})
public class DefectAct {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "deliveryId")
    private int deliveryId;

    public DefectAct(int id, int deliveryId) {
        this.id = id;
        this.deliveryId = deliveryId;
    }

    public int getId() {
        return id;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

}
