package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

public class ActViewModel extends AndroidViewModel {

    private Repository mRepository;

    private Delivery mDelivery;
    private MutableLiveData<List<DefectGood>> mDefects = new MutableLiveData<>();

    public ActViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;
    }

    public MutableLiveData<List<DefectGood>> getDefects() {
        return mDefects;
    }

    public void start(String deliveryId){
        mDelivery = mRepository.getDelivery(deliveryId);
        mDefects = mRepository.getDefectGoods(deliveryId);
    }

    public int getDeliveryId(){
        return mDelivery.getId();
    }
}
