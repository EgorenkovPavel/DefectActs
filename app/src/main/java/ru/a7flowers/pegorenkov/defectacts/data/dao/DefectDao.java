package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;

@Dao
public interface DefectDao {

    @Query("SELECT * FROM defects WHERE defectActId = :defectActId ")
    LiveData<List<Defect>> loadDefects(int defectActId);
}
