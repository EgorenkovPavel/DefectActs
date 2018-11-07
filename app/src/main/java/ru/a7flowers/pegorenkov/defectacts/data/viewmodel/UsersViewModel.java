package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;

public class UsersViewModel extends AndroidViewModel {

    private Repository mRepository;

    public UsersViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;
    }

    public LiveData<List<User>> getUsers() {
        return mRepository.getUsers();
    }

    public void setCurrentUser(User user) {
        mRepository.setCurrentUser(user);
    }
}
