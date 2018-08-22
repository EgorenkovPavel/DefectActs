package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.objects.Delivery;

public class DeliveriesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Delivery>> mDeliveries = new MutableLiveData<>();

    public DeliveriesViewModel(@NonNull Application application) {
        super(application);

        List<Delivery> deliveries = new ArrayList<>();

        deliveries.add(new Delivery("1", "CTQ342342", new Date(), 23, true));
        deliveries.add(new Delivery("1", "CTQ342342", new Date(), 23, false));
        deliveries.add(new Delivery("1", "CTQ342342", new Date(), 23, true));

        mDeliveries.postValue(deliveries);
    }

    public MutableLiveData<List<Delivery>> getDeliveries() {
        return mDeliveries;
    }
}
