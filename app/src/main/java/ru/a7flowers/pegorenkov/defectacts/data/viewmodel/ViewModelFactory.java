package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import ru.a7flowers.pegorenkov.defectacts.BuildConfig;
import ru.a7flowers.pegorenkov.defectacts.NetworkSettings;
import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.local.AppDatabase;
import ru.a7flowers.pegorenkov.defectacts.data.local.LocalDataSource;
import ru.a7flowers.pegorenkov.defectacts.data.network.NetworkDataSource;

public class ViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;
    private final Repository mRepository;

    public static ViewModelFactory getInstance(final Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {

                    NetworkSettings settings = new NetworkSettings(application);

                    INSTANCE = new ViewModelFactory(application,
                            Repository.getInstance(NetworkDataSource.getInstance(settings),
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

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass == UsersViewModel.class)
            return (T) new UsersViewModel(mApplication, mRepository);
        else if (modelClass == DeliveriesViewModel.class)
            return (T) new DeliveriesViewModel(mApplication, mRepository);
        else if (modelClass == DeliveryDefectViewModel.class)
            return (T) new DeliveryDefectViewModel(mApplication, mRepository);
        else if (modelClass == DeliveryDiffViewModel.class)
            return (T) new DeliveryDiffViewModel(mApplication, mRepository);
        else if (modelClass == DefectViewModel.class)
            return (T) new DefectViewModel(mApplication, mRepository);
        else if (modelClass == DiffViewModel.class)
            return (T) new DiffViewModel(mApplication, mRepository);
        else if (modelClass == ReasonsViewModel.class)
            return (T) new ReasonsViewModel(mApplication, mRepository);
        else if (modelClass == UploadPhotoViewModel.class)
            return (T) new UploadPhotoViewModel(mApplication, mRepository);

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
