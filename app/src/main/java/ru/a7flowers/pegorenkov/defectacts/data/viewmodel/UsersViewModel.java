package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;

public class UsersViewModel extends AndroidViewModel {

    private Repository mRepository;

    private MutableLiveData<Boolean> isReloading = new MutableLiveData<>();

    public UsersViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;

        isReloading.postValue(false);
    }

    public LiveData<List<User>> getUsers() {
        return mRepository.getUsers();
    }

    public void setCurrentUser(User user) {
        mRepository.setCurrentUser(user);
    }

    public void refreshData() {
        isReloading.postValue(true);
        mRepository.reloadData(new DataSource.ReloadDataCallback() {
            @Override
            public void onDataReloaded() {
                isReloading.setValue(false);
            }

            @Override
            public void onDataReloadingFailed() {
                isReloading.setValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getIsReloading() {
        return isReloading;
    }

}
