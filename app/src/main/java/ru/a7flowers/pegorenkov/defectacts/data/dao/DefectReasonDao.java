package ru.a7flowers.pegorenkov.defectacts.data.dao;

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
            "FROM reasons as reasons " +
            "INNER JOIN defectreasons as defectreasons " +
            "ON reasons.id = defectreasons.reasonId " +
            "WHERE defectreasons.defectId = :defectId")
    List<Reason> loadReasons(String defectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReason(DefectReason reason);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReasons(List<DefectReason> reasons);

    @Query("DELETE FROM defectreasons WHERE defectId = :defectId")
    void deleteReasons(String defectId);

}
