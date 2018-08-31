package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;

public class DeliveriesViewModel extends AndroidViewModel {

    @SuppressWarnings("FieldCanBeLocal")
    private Repository mRepository;

    private LiveData<List<Delivery>> mDeliveries;
    private MutableLiveData<List<Delivery>> mSelectedDeliveries = new MutableLiveData<>();

    public DeliveriesViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        mDeliveries = mRepository.getDeliveries();
        mSelectedDeliveries.postValue(new ArrayList<Delivery>());
    }

    public LiveData<List<Delivery>> getDeliveries() {
        return mDeliveries;
    }

    public MutableLiveData<List<Delivery>> getSelectedDeliveries() {
        return mSelectedDeliveries;
    }

    public void onDeliverySelected(Delivery delivery) {
        List<Delivery> list = mSelectedDeliveries.getValue();
        if (list.contains(delivery))
            list.remove(delivery);
        else
            list.add(delivery);
        mSelectedDeliveries.postValue(list);
    }
}
