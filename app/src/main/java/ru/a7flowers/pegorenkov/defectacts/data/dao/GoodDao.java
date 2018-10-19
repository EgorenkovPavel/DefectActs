package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.GoodEntity;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

@Dao
public interface GoodDao {

    @Query("SELECT * FROM goods WHERE deliveryid IN (:deliveryIds) ")
    LiveData<List<GoodEntity>> loadGoodEntities(String[] deliveryIds);

    @Query("SELECT * FROM goods WHERE deliveryid =:deliveryId AND series =:series ")
    GoodEntity loadGoodEntity(String deliveryId, String series);

    @Transaction
    @Query("SELECT * FROM goods WHERE deliveryid IN (:deliveryIds) ")
    LiveData<List<Good>> loadGoods(String[] deliveryIds);

    @Transaction
    @Query("SELECT * FROM goods WHERE deliveryid =:deliveryId AND series =:series ")
    Good loadGood(String deliveryId, String series);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGood(GoodEntity good);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGoods(List<GoodEntity> goods);

}
