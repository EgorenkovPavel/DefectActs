package ru.a7flowers.pegorenkov.defectacts.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class NetworkDataSource implements DataSource {

    private static volatile NetworkDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    private NetworkDataSource() {
        mAppExecutors = AppExecutors.getInstance();
    }

    public static NetworkDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (NetworkDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetworkDataSource();
                }
            }
        }
        return INSTANCE;
    }

    public void loadDeliveries(LoadDeliveriesCallback callback){
        List<Delivery> list = new ArrayList<>();
        list.add(new Delivery(1, "sdfdsdfasdf", "CTQ234234", new Date(), true));

        callback.onDeliveriesLoaded(list);
    }

    public void loadGoods(Delivery delivery, LoadGoodsCallback callback){
        List<Good> list = new ArrayList<>();

        list.add(new Good(2, "1111111111111", "Rose", "OZ", "Vietnam", 1, delivery.getId()));
        list.add(new Good(23, "234234234234", "Rose", "OZ", "Vietnam", 2, delivery.getId()));
        list.add(new Good(3, "3333333333333", "Rose", "OZ", "Vietnam", 3, delivery.getId()));

        callback.onGoodsLoaded(list);
    }

    public void loadDefects(Delivery delivery, LoadDefectsCallback callback){
        List<Defect> list = new ArrayList<>();

        list.add(new Defect(34, "dfsf334fd34f", "234234234234", 34, 5 , delivery.getId(), "comment1"));

        callback.onDefectsLoaded(list);
    }

    public void loadReasons(LoadReasonsCallback callback){
        List<Reason> list = new ArrayList<>();

        list.add(new Reason(1, "asdfsdasda", "reason 1"));
        list.add(new Reason(2, "asdfsdssa", "reason 2"));
        list.add(new Reason(3, "asdfsd55da", "reason 3"));

        callback.onReasonsLoaded(list);
    }

    public void createAct(Delivery delivery) {
    }

    public void saveDefect(Defect defect, List<Reason> reasons) {
    }

    public void savePhotos(List<String> photoPaths) {
    }
}
