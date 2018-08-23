package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.objects.Defect;

public class ActViewModel extends AndroidViewModel {

    private Repository mRepository;
    private MutableLiveData<List<Defect>> mDefects = new MutableLiveData<>();

    public ActViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        mDefects.postValue(mRepository.getDefects());
    }

    public MutableLiveData<List<Defect>> getDefects() {
        return mDefects;
    }
}
