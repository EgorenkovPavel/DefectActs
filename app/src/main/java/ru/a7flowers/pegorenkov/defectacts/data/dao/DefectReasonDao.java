package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

@Dao
public interface DefectReasonDao {

    @Query("SELECT reasons.* " +
            "FROM defectreasons as defectreasons " +
            "INNER JOIN reasons as reasons " +
            "ON defectreasons.reasonId = reasons.id " +
            "WHERE defectreasons.defectId = :defectId")
    List<Reason> loadReasons(String defectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReason(DefectReason reason);
}
