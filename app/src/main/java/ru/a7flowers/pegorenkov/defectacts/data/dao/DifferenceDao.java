package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.DifferenceEntity;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;

@Dao
public interface DifferenceDao {

    @Transaction
    @Query("SELECT differencies.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "goods.deliveryQuantity as deliveryQuantity, " +
            "deliveries.number as deliveryNumber " +
            "FROM differencies as differencies " +
            "INNER JOIN goods as goods " +
            "ON differencies.series = goods.series " +
            "AND differencies.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON differencies.deliveryId = deliveries.id " +
            "WHERE differencies.deliveryId IN (:deliveryIds)" +
            "ORDER BY goods.series")
    LiveData<List<Diff>> loadDifferencies(String[] deliveryIds);

    @Transaction
    @Query("SELECT differencies.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "goods.deliveryQuantity as deliveryQuantity, " +
            "deliveries.number as deliveryNumber " +
            "FROM differencies as differencies " +
            "INNER JOIN goods as goods " +
            "ON differencies.series = goods.series " +
            "AND differencies.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON differencies.deliveryId = deliveries.id " +
            "WHERE differencies.deliveryId = :deliveryId " +
            "AND differencies.series = :series " +
            "AND differencies.id != :exeptedDiffId " +
            "ORDER BY goods.series")
    List<Diff> loadDiffsByGood(String deliveryId, String series, String exeptedDiffId);

    @Transaction
    @Query("SELECT differencies.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "goods.deliveryQuantity as deliveryQuantity, " +
            "deliveries.number as deliveryNumber " +
            "FROM differencies as differencies " +
            "INNER JOIN goods as goods " +
            "ON differencies.series = goods.series " +
            "AND differencies.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON differencies.deliveryId = deliveries.id " +
            "WHERE differencies.id = :defectId")
    Diff loadDifference(String defectId);

    @Transaction
    @Query("SELECT differencies.*, " +
            "goods.good as title, " +
            "goods.suplier as suplier, " +
            "goods.country as country, " +
            "goods.deliveryQuantity as deliveryQuantity, " +
            "deliveries.number as deliveryNumber " +
            "FROM differencies as differencies " +
            "INNER JOIN goods as goods " +
            "ON differencies.series = goods.series " +
            "AND differencies.deliveryId = goods.deliveryId " +
            "INNER JOIN deliveries as deliveries " +
            "ON differencies.deliveryId = deliveries.id ")
    LiveData<List<Diff>> loadDifferencies();

    @Query("SELECT * FROM differencies WHERE id = :diffId")
    DifferenceEntity getDifferenceById(int diffId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDifference(DifferenceEntity diff);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDifferencies(List<DifferenceEntity> diffs);

    @Query("UPDATE differencies SET photoQuantity = :photoCount WHERE deliveryId = :deliveryId AND id = :defectId")
    void setPhotoCount(String deliveryId, String defectId, int photoCount);
}
