package ru.a7flowers.pegorenkov.defectacts.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.objects.Delivery;

public class Repository {

    private volatile static Repository INSTANCE = null;

    private DataSource mLocalDataSource;

    private Repository(NetworkDataSource localDataSource){
        mLocalDataSource = localDataSource;
    }

    public static Repository getInstance(NetworkDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository(localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public List<Delivery> getDeliveries(){
        List<Delivery> deliveries = new ArrayList<>();

        deliveries.add(new Delivery("1", "CTQ342342", new Date(), 23, true));
        deliveries.add(new Delivery("1", "CTQ342342", new Date(), 23, false));
        deliveries.add(new Delivery("1", "CTQ342342", new Date(), 23, true));

        return deliveries;
    }
}
