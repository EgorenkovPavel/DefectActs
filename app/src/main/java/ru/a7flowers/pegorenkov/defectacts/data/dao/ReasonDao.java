package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

@Dao
public interface ReasonDao {

    @Query("SELECT * FROM reasons ORDER BY title")
    LiveData<List<Reason>> loadReasons();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReason(Reason reason);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReasons(List<Reason> reasons);

    @Query("DELETE FROM reasons")
    void deleteAll();
}
