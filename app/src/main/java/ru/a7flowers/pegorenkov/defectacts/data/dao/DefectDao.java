package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

@Dao
public interface DefectDao {

    @Query("SELECT goods.series, goods.good, goods.suplier, goods.country, goods.deliveryQuantity,  " +
            "defects.* " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId " +
            "WHERE defects.deliveryId = :deliveryId")
    LiveData<List<DefectGood>> loadDefects(String deliveryId);

    @Query("SELECT * FROM defects WHERE id = :defectId")
    Defect getDefectById(int defectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDefect(Defect defect);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDefects(List<Defect> defects);
}
