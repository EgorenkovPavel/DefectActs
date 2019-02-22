package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.UploadPhotoEntity;

@Dao
public interface UploadPhotoDao {

    @Query("SELECT * FROM uploadPhotos WHERE try_number < :maxTryNumber")
    List<UploadPhotoEntity> getUploadPhotos(int maxTryNumber);

    @Update
    void update(UploadPhotoEntity entity);

    @Delete
    void deleteUploadPhoto(List<UploadPhotoEntity> entities);

}
