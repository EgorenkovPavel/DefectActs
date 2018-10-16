package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;

public class DeliveryDiffViewModel extends AndroidViewModel {

    private Repository mRepository;

    private String[] mDeliveries;
    private LiveData<List<Diff>> mDiffs;

    public DeliveryDiffViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;
    }

    public void start(String[] deliveries){
        mDeliveries = deliveries;
        mDiffs = mRepository.getDiffGoods(mDeliveries);
    }

    public LiveData<List<Diff>> getDiffs() {
        return mDiffs;
    }

    public String[] getDeliveryIds(){
        return mDeliveries;
    }

}
