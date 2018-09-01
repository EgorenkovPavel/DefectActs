package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;

public class DeliveriesViewModel extends AndroidViewModel {

    @SuppressWarnings("FieldCanBeLocal")
    private Repository mRepository;

    private LiveData<List<Delivery>> mDeliveries;
    private Set<String> mSelectedDeliveriesIds = new HashSet<>();

    public DeliveriesViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        mDeliveries = mRepository.getDeliveries();
    }

    public LiveData<List<Delivery>> getDeliveries() {
        return mDeliveries;
    }

    public String[] getSelectedDeliveryIds() {
        return mSelectedDeliveriesIds.toArray(new String[mSelectedDeliveriesIds.size()]);
    }

    public boolean isDeliverySelected(Delivery delivery) {
        return mSelectedDeliveriesIds.contains(delivery.getId());
    }

    public void addSelectedDelivery(Delivery delivery) {
        mSelectedDeliveriesIds.add(delivery.getId());
    }

    public void removeSelectedDelivery(Delivery delivery) {
        mSelectedDeliveriesIds.remove(delivery.getId());
    }
}
