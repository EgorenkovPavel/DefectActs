package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.GoodEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBudgeonAmountEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBulkEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueDiameterEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueLengthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueWeigthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

@Dao
public interface GoodDao {

    @Query("SELECT * FROM goods WHERE deliveryid IN (:deliveryIds) ")
    LiveData<List<GoodEntity>> loadGoodEntities(String[] deliveryIds);

    @Query("SELECT * FROM goods WHERE deliveryid =:deliveryId AND series =:series ")
    GoodEntity loadGoodEntity(String deliveryId, String series);

    @Transaction
    @Query("SELECT goods.*, " +
            "deliveries.number as deliveryNumber " +
            "FROM goods as goods " +
            "INNER JOIN deliveries as deliveries " +
            "ON deliveryId = id " +
            "WHERE deliveryid IN (:deliveryIds) ")
    LiveData<List<Good>> loadGoods(String[] deliveryIds);

    @Transaction
    @Query("SELECT goods.*, " +
            "deliveries.number as deliveryNumber " +
            "FROM goods as goods " +
            "INNER JOIN deliveries as deliveries " +
            "ON deliveryId = id " +
            "WHERE deliveryid =:deliveryId AND series =:series ")
    Good loadGood(String deliveryId, String series);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGood(GoodEntity good);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGoods(List<GoodEntity> goods);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDiameters(List<ValueDiameterEntity> diameters);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLengths(List<ValueLengthEntity> lengths);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeigths(List<ValueWeigthEntity> weigths);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBudgeonAmounts(List<ValueBudgeonAmountEntity> budgeonAmounts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBulk(List<ValueBulkEntity> bulks);

    @Query("DELETE FROM diameter")
    void deleteAllDiameters();

    @Query("DELETE FROM length")
    void deleteAllLengths();

    @Query("DELETE FROM weigth")
    void deleteAllWeigths();

    @Query("DELETE FROM budgeonAmount")
    void deleteAllBudgeonsAmounts();

    @Query("DELETE FROM bulk")
    void deleteAllBulks();
}
