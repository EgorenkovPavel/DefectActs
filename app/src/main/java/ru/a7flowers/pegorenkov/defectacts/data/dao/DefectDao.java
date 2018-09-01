package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;

@Dao
public interface DefectDao {

    @Transaction
    @Query("SELECT defects.*, " +
            "goods.series as good_series, "
            + "goods.good as good_good, "
            + "goods.suplier as good_suplier, "
            + "goods.country as good_country, "
            + "goods.deliveryId as good_deliveryId, "
            + "goods.deliveryQuantity as good_deliveryQuantity " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId " +
            "WHERE defects.deliveryId IN (:deliveryIds)")
    LiveData<List<DefectWithReasons>> loadDefects(String[] deliveryIds);

    @Transaction
    @Query("SELECT defects.*, " +
            "goods.series as good_series, "
            + "goods.good as good_good, "
            + "goods.suplier as good_suplier, "
            + "goods.country as good_country, "
            + "goods.deliveryId as good_deliveryId, "
            + "goods.deliveryQuantity as good_deliveryQuantity " +
            "FROM defects as defects " +
            "INNER JOIN goods as goods " +
            "ON defects.series = goods.series " +
            "AND defects.deliveryId = goods.deliveryId ")
    LiveData<List<DefectWithReasons>> loadDefectServer();

    @Query("SELECT * FROM defects WHERE id = :defectId")
    Defect getDefectById(int defectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDefect(Defect defect);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDefects(List<Defect> defects);
}
