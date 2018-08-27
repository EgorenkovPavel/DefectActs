package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "defectreasons",
foreignKeys = {@ForeignKey(entity = Defect.class, parentColumns = "id", childColumns = "defectId"),
@ForeignKey(entity = Reason.class, parentColumns = "id", childColumns = "reasonId")},
indices = {@Index("reasonId"), @Index("defectId")})
public class DefectReason {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "defectId")
    private int defectId;
    @ColumnInfo(name = "reasonId")
    private int reasonId;

    public DefectReason(int id, int defectId, int reasonId) {
        this.id = id;
        this.defectId = defectId;
        this.reasonId = reasonId;
    }

    public DefectReason(int defectId, int reasonId) {
        this.defectId = defectId;
        this.reasonId = reasonId;
    }

    public int getId() {
        return id;
    }

    public int getDefectId() {
        return defectId;
    }

    public int getReasonId() {
        return reasonId;
    }
}
