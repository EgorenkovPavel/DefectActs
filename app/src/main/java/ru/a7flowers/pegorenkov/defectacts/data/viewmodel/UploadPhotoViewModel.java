package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.UploadPhotoEntity;

public class UploadPhotoViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<UploadPhotoEntity>> failedPhotos;

    public UploadPhotoViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        failedPhotos = mRepository.getFailedUploadPhotos();
    }

    public LiveData<List<UploadPhotoEntity>> getFailedPhotos() {
        return failedPhotos;
    }

    public void cancelFailedUploadPhoto(){
        mRepository.cancelFailedUploadPhoto(failedPhotos.getValue());
    }

    public void retryFailedUploadPhoto() {
        mRepository.retryFailedUploadPhoto(failedPhotos.getValue());
    }

    public void deleteAllUploadPhoto() {

    }
}
