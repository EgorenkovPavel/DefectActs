package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import retrofit2.http.DELETE;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;

@Dao
public interface DeliveryDao {

    @Query("SELECT * FROM deliveries ORDER BY date")
    LiveData<List<Delivery>> loadAllDeliveries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDelivery(Delivery delivery);

    @Query("SELECT * FROM deliveries WHERE id = :deliveryId")
    LiveData<Delivery> getDeliveryById(int deliveryId);

    @Query("DELETE FROM deliveries")
    void deleteAllDeliveries();
}
