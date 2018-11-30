package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectEntity;
import ru.a7flowers.pegorenkov.defectacts.data.network.Defect;

@Dao
public interface DefectDao {

    @Transaction
    @Query("SELECT defects.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "goods.deliveryQuantity as deliveryQuantity, " +
            "deliveries.number as deliveryNumber " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON defects.deliveryId = deliveries.id " +
            "WHERE defects.deliveryId IN (:deliveryIds)" +
            "ORDER BY goods.series")
    LiveData<List<Defect>> loadDefects(String[] deliveryIds);

    @Transaction
    @Query("SELECT defects.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "goods.deliveryQuantity as deliveryQuantity, " +
            "deliveries.number as deliveryNumber " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON defects.deliveryId = deliveries.id " +
            "WHERE defects.deliveryId = :deliveryId " +
            "AND defects.series = :series " +
            "AND defects.id != :exeptedDefectId " +
            "ORDER BY goods.series")
    List<Defect> loadDefectsByGood(String deliveryId, String series, String exeptedDefectId);

    @Transaction
    @Query("SELECT defects.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "goods.deliveryQuantity as deliveryQuantity, " +
            "deliveries.number as deliveryNumber " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON defects.deliveryId = deliveries.id " +
            "WHERE defects.id = :defectId")
    Defect loadDefect(String defectId);

    @Transaction
    @Query("SELECT defects.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "goods.deliveryQuantity as deliveryQuantity, " +
            "deliveries.number as deliveryNumber " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON defects.deliveryId = deliveries.id ")
    LiveData<List<Defect>> loadDefectServer();

    @Query("SELECT * FROM defects WHERE id = :defectId")
    DefectEntity getDefectById(int defectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDefect(DefectEntity defect);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDefects(List<DefectEntity> defects);

    @Query("UPDATE defects SET photoQuantity = :photoCount WHERE deliveryId = :deliveryId AND id = :defectId")
    void setPhotoCount(String deliveryId, String defectId, int photoCount);

}
