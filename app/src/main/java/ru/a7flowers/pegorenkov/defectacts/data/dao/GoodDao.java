package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;

@Dao
public interface GoodDao {

    @Query("SELECT * FROM goods WHERE deliveryid = :deliveryId ")
    LiveData<List<Good>> loadGoods(int deliveryId);
}
