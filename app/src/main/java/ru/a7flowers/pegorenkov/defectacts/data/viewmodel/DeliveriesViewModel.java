package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;

public class DeliveriesViewModel extends AndroidViewModel {

    private Repository mRepository;

    private MutableLiveData<List<Delivery>> mDeliveries = new MutableLiveData<>();

    public DeliveriesViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        mDeliveries.postValue(mRepository.getDeliveries());
    }

    public MutableLiveData<List<Delivery>> getDeliveries() {
        return mDeliveries;
    }
}
