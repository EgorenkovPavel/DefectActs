package ru.a7flowers.pegorenkov.defectacts.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.UploadPhotoEntity;

@Dao
public interface UploadPhotoDao {

    @Query("SELECT * FROM uploadPhotos")
    LiveData<List<UploadPhotoEntity>> getAllUploadPhotos();

    @Query("SELECT * FROM uploadPhotos WHERE try_number < :maxTryNumber")
    List<UploadPhotoEntity> getUploadPhotos(int maxTryNumber);

    @Query("SELECT * FROM uploadPhotos WHERE try_number >= :maxTryNumber")
    LiveData<List<UploadPhotoEntity>> getFailedUploadPhotos(int maxTryNumber);

    @Insert
    void insert(List<UploadPhotoEntity> entities);

    @Update
    void updatePhoto(UploadPhotoEntity entity);

    @Update
    void updatePhotos(List<UploadPhotoEntity> entities);

    @Delete
    void deletePhoto(UploadPhotoEntity entity);

    @Delete
    void deletePhotos(List<UploadPhotoEntity> entities);

    @Query("DELETE FROM uploadPhotos WHERE try_number >= :maxTryNumber")
    void clearPhotos(int maxTryNumber);

    @Query("DELETE FROM uploadPhotos")
    void deleteAllPhotos();

}
