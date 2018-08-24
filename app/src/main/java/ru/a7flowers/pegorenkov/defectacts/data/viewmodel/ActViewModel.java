package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.objects.Defect;
import ru.a7flowers.pegorenkov.defectacts.objects.Delivery;

public class ActViewModel extends AndroidViewModel {

    private Repository mRepository;

    private Delivery mDelivery;
    private MutableLiveData<List<Defect>> mDefects = new MutableLiveData<>();

    public ActViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;
    }

    public MutableLiveData<List<Defect>> getDefects() {
        return mDefects;
    }

    public void start(String id){
        mDelivery = mRepository.getDelivery(id);
        mDefects.postValue(mDelivery.getDefectAct().getDefects());
    }

    public String getDeliveryId(){
        return mDelivery.getId();
    }
}
