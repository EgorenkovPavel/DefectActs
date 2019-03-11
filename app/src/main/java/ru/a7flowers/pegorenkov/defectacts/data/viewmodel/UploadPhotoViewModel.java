package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.UploadPhotoEntity;

public class UploadPhotoViewModel extends AndroidViewModel {

    private Repository mRepository;

    private LiveData<List<UploadPhotoEntity>> mPhotos;
    private LiveData<List<UploadPhotoEntity>> mFailedPhotos;
    private SingleLiveEvent<Boolean> isClose = new SingleLiveEvent<>();;
    private String appPhotoDir;

    public UploadPhotoViewModel(@NonNull Application application, Repository repository) {
        super(application);

        appPhotoDir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        mRepository = repository;

        mPhotos = mRepository.getAllUploadPhotos();
        mFailedPhotos = mRepository.getFailedUploadPhotos();
    }

    public LiveData<List<UploadPhotoEntity>> getPhotos() {
        return mPhotos;
    }

    public LiveData<List<UploadPhotoEntity>> getFailedPhotos() {
        return mFailedPhotos;
    }

    public SingleLiveEvent<Boolean> isClose() {
        return isClose;
    }

    private void deleteFiles(String dirPath){
        File f = new File(dirPath);
        File[] files = f.listFiles();
        for (File file:files) {
            file.delete();
        }
    }

    public void uploadAllPhotos() {
        mRepository.startPhotoUploading();
        isClose.postValue(true);
    }

    public void clearAllPhotos() {
        mRepository.deleteAllUploadPhoto(()->{
            deleteFiles(appPhotoDir);
            isClose.postValue(true);
        });
    }

    public void uploadFailedPhotos() {
        mRepository.retryFailedUploadPhoto(mFailedPhotos.getValue(), ()->isClose.postValue(true));
    }

    public void clearFailedPhotos() {
        List<UploadPhotoEntity> entities = mFailedPhotos.getValue();
        for (UploadPhotoEntity entity:entities) {
            File f = new File(entity.getPhotoPath());
            f.delete();
        }
        mRepository.clearFailedUploadPhoto(()-> isClose.postValue(true));
    }
}
