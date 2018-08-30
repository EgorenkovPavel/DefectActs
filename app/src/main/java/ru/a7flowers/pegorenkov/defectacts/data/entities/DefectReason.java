package ru.a7flowers.pegorenkov.defectacts.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

@Entity(tableName = "defectreasons",
primaryKeys = {"defectId", "reasonId"},
foreignKeys = {@ForeignKey(entity = Defect.class, parentColumns = "id", childColumns = "defectId", onDelete = ForeignKey.CASCADE),
@ForeignKey(entity = Reason.class, parentColumns = "id", childColumns = "reasonId", onDelete = ForeignKey.CASCADE)},
indices = {@Index("reasonId"), @Index("defectId")})
public class DefectReason {

    @ColumnInfo(name = "defectId")
    @NonNull
    private String defectId;

    @ColumnInfo(name = "reasonId")
    @NonNull
    private String reasonId;

    public DefectReason(String defectId, String reasonId) {
        this.defectId = defectId;
        this.reasonId = reasonId;
    }

    public String getDefectId() {
        return defectId;
    }

    public String getReasonId() {
        return reasonId;
    }
}
