package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ru.a7flowers.pegorenkov.defectacts.data.NetworkDataSource;
import ru.a7flowers.pegorenkov.defectacts.data.Repository;

public class ViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;
    private final Repository mRepository;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application,
                            Repository.getInstance(NetworkDataSource.getInstance()));
                }
            }
        }
        return INSTANCE;
    }

    private ViewModelFactory(@NonNull Application application, Repository repository) {
        super(application);
        mApplication = application;
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass == DeliveriesViewModel.class)
            return (T) new DeliveriesViewModel(mApplication, mRepository);

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
