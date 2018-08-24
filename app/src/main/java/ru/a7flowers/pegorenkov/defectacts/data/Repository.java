package ru.a7flowers.pegorenkov.defectacts.data;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.objects.Defect;
import ru.a7flowers.pegorenkov.defectacts.objects.DefectAct;
import ru.a7flowers.pegorenkov.defectacts.objects.Delivery;
import ru.a7flowers.pegorenkov.defectacts.objects.Good;
import ru.a7flowers.pegorenkov.defectacts.objects.Reason;

public class Repository {

    private volatile static Repository INSTANCE = null;

    private NetworkDataSource mNetworkDataSource;

    private List<Delivery> mDeliveries;
    private List<Reason> mReasons;

    private Repository(NetworkDataSource networkDataSource){
        mNetworkDataSource = networkDataSource;
    }

    public static Repository getInstance(NetworkDataSource networkDataSource) {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository(networkDataSource);
                }
            }
        }
        return INSTANCE;
    }

    public List<Delivery> getDeliveries(){
        if(mDeliveries == null){
//            mNetworkDataSource.getDeliveries();
            mDeliveries = new ArrayList<>();

            Delivery delivery1 = new Delivery("1", "CTQ342341", new Date());
            delivery1.setGoods(getGoods());
            mDeliveries.add(delivery1);
            mDeliveries.add(new Delivery("2", "CTQ342342", new Date()));
            mDeliveries.add(new Delivery("3", "CTQ342343", new Date()));
        }

        return mDeliveries;
    }

    public Delivery getDelivery(String id){
        //TODO add callback
        for (Delivery delivery:mDeliveries) {
            if(delivery.getId().equals(id)){
                if(delivery.getDefectAct() == null){
                    delivery.setDefectAct(new DefectAct());
                }
                return delivery;
            }
        }
        return null;
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
        if(mReasons == null){
//            mNetworkDataSource.getReasons();
            mReasons = new ArrayList<>();
            mReasons.add(new Reason("2", "reason1"));
            mReasons.add(new Reason("2", "reason2"));
            mReasons.add(new Reason("2", "reason3"));
        }

        return mReasons;
    }

    public void saveDefect(Delivery delivery, Defect defect){
        delivery.getDefectAct().addDefect(defect);
    }
}
