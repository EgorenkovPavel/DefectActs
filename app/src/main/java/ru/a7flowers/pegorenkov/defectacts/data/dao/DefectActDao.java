package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectAct;

@Dao
public interface DefectActDao {

    @Query("SELECT * FROM defectacts WHERE deliveryid = :deliveryId ")
    LiveData<DefectAct> loadDefectAct(int deliveryId);
}
