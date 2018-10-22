package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectEntity;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;

@Dao
public interface DefectDao {

    @Transaction
    @Query("SELECT defects.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "deliveries.number as deliveryNumber " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON defects.deliveryId = deliveries.id " +
            "WHERE defects.deliveryId IN (:deliveryIds)")
    LiveData<List<DefectWithReasons>> loadDefects(String[] deliveryIds);

    @Transaction
    @Query("SELECT defects.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "deliveries.number as deliveryNumber " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON defects.deliveryId = deliveries.id " +
            "WHERE defects.id = :defectId")
    DefectWithReasons loadDefect(String defectId);

    @Transaction
    @Query("SELECT defects.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "deliveries.number as deliveryNumber " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON defects.deliveryId = deliveries.id ")
    LiveData<List<DefectWithReasons>> loadDefectServer();

    @Query("SELECT * FROM defects WHERE id = :defectId")
    DefectEntity getDefectById(int defectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDefect(DefectEntity defect);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDefects(List<DefectEntity> defects);
}
