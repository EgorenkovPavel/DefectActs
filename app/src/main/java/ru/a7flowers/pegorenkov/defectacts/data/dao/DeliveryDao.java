package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;

@Dao
public interface DeliveryDao {

    @Query("SELECT * FROM deliveries ORDER BY date")
    LiveData<List<Delivery>> loadAllDeliveries();

}
