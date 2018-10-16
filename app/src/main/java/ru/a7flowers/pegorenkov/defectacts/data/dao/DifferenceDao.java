package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DifferenceEntity;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;
import ru.a7flowers.pegorenkov.defectacts.data.network.Difference;

@Dao
public interface DifferenceDao {

    @Transaction
    @Query("SELECT differencies.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country " +
            "FROM differencies as differencies " +
            "INNER JOIN goods as goods " +
            "ON differencies.series = goods.series " +
            "AND differencies.deliveryId = goods.deliveryId " +
            "WHERE differencies.deliveryId IN (:deliveryIds)")
    LiveData<List<Difference>> loadDifferencies(String[] deliveryIds);

    @Transaction
    @Query("SELECT differencies.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country " +
            "FROM differencies as differencies " +
            "INNER JOIN goods as goods " +
            "ON differencies.series = goods.series " +
            "AND differencies.deliveryId = goods.deliveryId " +
            "WHERE differencies.id = :defectId")
    Difference loadDifference(String defectId);

    @Transaction
    @Query("SELECT differencies.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country " +
            "FROM differencies as differencies " +
            "INNER JOIN goods as goods " +
            "ON differencies.series = goods.series " +
            "AND differencies.deliveryId = goods.deliveryId ")
    LiveData<List<Difference>> loadDifferencies();

    @Query("SELECT * FROM differencies WHERE id = :diffId")
    DifferenceEntity getDifferenceById(int diffId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDifference(DifferenceEntity diff);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDifferencies(List<DifferenceEntity> diffs);
}
