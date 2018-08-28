package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
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
    private String defectId;
    @ColumnInfo(name = "reasonId")
    private String reasonId;

    public DefectReason(int id, String defectId, String reasonId) {
        this.id = id;
        this.defectId = defectId;
        this.reasonId = reasonId;
    }

    @Ignore
    public DefectReason(String defectId, String reasonId) {
        this.defectId = defectId;
        this.reasonId = reasonId;
    }

    public int getId() {
        return id;
    }

    public String getDefectId() {
        return defectId;
    }

    public String getReasonId() {
        return reasonId;
    }
}
