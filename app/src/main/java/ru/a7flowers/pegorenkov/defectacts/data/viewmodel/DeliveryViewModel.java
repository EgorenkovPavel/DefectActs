package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

public class DeliveryViewModel extends AndroidViewModel {

    private Repository mRepository;

    private Delivery mDelivery;
    private LiveData<List<DefectGood>> mDefects;

    public DeliveryViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;
    }

    public LiveData<List<DefectGood>> getDefects() {
        return mDefects;
    }

    public void start(Delivery delivery){
        mDelivery = delivery;

        mDefects = mRepository.getDefectGoods(mDelivery);
    }

    public Delivery getDelivery(){
        return mDelivery;
    }

}
