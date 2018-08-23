package ru.a7flowers.pegorenkov.defectacts.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.objects.Defect;
import ru.a7flowers.pegorenkov.defectacts.objects.Delivery;
import ru.a7flowers.pegorenkov.defectacts.objects.Good;
import ru.a7flowers.pegorenkov.defectacts.objects.Reason;

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

    public List<Defect> getDefects(){
        ArrayList<Defect> defects = new ArrayList<>();
        defects.add(new Defect("1", "55000323423423", "Rose", "OZ", "Belgium", 43, 5));
        defects.add(new Defect("1", "55000323423423", "Rose", "OZ", "Belgium", 43, 5));
        defects.add(new Defect("1", "55000323423423", "Rose", "OZ", "Belgium", 43, 5));

        return defects;
    }

    public List<Good> getGoods(){
        ArrayList<Good> mGoods = new ArrayList<>();
        mGoods.add(new Good("1", "55000002123123", "Rose", "Boston", "Russia", 21));
        mGoods.add(new Good("1", "55000002123123", "Rose", "Boston", "Russia", 22));
        mGoods.add(new Good("1", "55000002123123", "Rose", "Boston", "Russia", 23));
        mGoods.add(new Good("1", "55000002123123", "Rose", "Boston", "Russia", 24));

        return mGoods;
    }

    public List<Reason> getReasons(){
        ArrayList<Reason> reasons = new ArrayList<>();
        reasons.add(new Reason("2", "reason1"));
        reasons.add(new Reason("2", "reason2"));
        reasons.add(new Reason("2", "reason3"));

        return reasons;
    }
}
