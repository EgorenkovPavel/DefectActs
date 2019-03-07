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

    private LiveData<List<UploadPhotoEntity>> failedPhotos;
    private SingleLiveEvent<Boolean> isClose = new SingleLiveEvent<>();;
    private String appPhotoDir;

    public UploadPhotoViewModel(@NonNull Application application, Repository repository) {
        super(application);

        appPhotoDir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        mRepository = repository;

        failedPhotos = mRepository.getFailedUploadPhotos();
    }

    public LiveData<List<UploadPhotoEntity>> getFailedPhotos() {
        return failedPhotos;
    }

    public SingleLiveEvent<Boolean> isClose() {
        return isClose;
    }

    public void clearFailedUploadPhoto(){
        List<UploadPhotoEntity> entities = failedPhotos.getValue();
        for (UploadPhotoEntity entity:entities) {
            File f = new File(entity.getPhotoPath());
            f.delete();
        }

        mRepository.clearFailedUploadPhoto(()->isClose.postValue(true));
    }

    public void retryFailedUploadPhoto() {
        mRepository.retryFailedUploadPhoto(failedPhotos.getValue(), ()->isClose.postValue(true));
    }

    public void deleteAllUploadPhoto() {
        mRepository.deleteAllUploadPhoto(()->{
            deleteFiles(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
            deleteFiles(appPhotoDir);
            isClose.postValue(true);
        });
    }

    private void deleteFiles(String dirPath){
        File f = new File(dirPath);
        File[] files = f.listFiles();
        for (File file:files) {
            file.delete();
        }
    }
}
