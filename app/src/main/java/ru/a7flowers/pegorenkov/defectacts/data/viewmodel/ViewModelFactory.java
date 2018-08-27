package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ru.a7flowers.pegorenkov.defectacts.data.AppDatabase;
import ru.a7flowers.pegorenkov.defectacts.data.LocalDataSource;
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
                            Repository.getInstance(NetworkDataSource.getInstance(),
                            LocalDataSource.getInstance(AppDatabase.getInstance(application))));
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
        else if (modelClass == DeliveryViewModel.class)
            return (T) new DeliveryViewModel(mApplication, mRepository);
        else if (modelClass == DefectViewModel.class)
            return (T) new DefectViewModel(mApplication, mRepository);
        else if (modelClass == ReasonsViewModel.class)
            return (T) new ReasonsViewModel(mApplication, mRepository);

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
