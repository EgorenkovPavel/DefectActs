package ru.a7flowers.pegorenkov.defectacts.data;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

public class Repository implements DataSource.LoadDeliveriesCallback, DataSource.LoadGoodsCallback, DataSource.LoadDefectsCallback, DataSource.LoadReasonsCallback {

    private volatile static Repository INSTANCE = null;

    private NetworkDataSource mNetworkDataSource;
    private LocalDataSource mLocalDataSource;

    private LiveData<List<Delivery>> mDeliveries;
    private LiveData<List<Reason>> mReasons;

    private Repository(NetworkDataSource networkDataSource, LocalDataSource localDataSource){
        mNetworkDataSource = networkDataSource;
        mLocalDataSource = localDataSource;
    }

    public static Repository getInstance(NetworkDataSource networkDataSource, LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository(networkDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    // DELIVERY

    public LiveData<List<Delivery>> getDeliveries(){
        if(mDeliveries == null){
            loadDeliveriesFromNetwork();
            mDeliveries = mLocalDataSource.getDeliveries();
        }
        return mDeliveries;
    }

    private void loadDeliveriesFromNetwork(){
        mNetworkDataSource.loadDeliveries(this);
    }

    public LiveData<Delivery> getDelivery(int deliveryId){
        return mLocalDataSource.getDelivery(deliveryId);
    }

    @Override
    public void onDeliveriesLoaded(List<Delivery> deliveries) {
        for (Delivery delivery:deliveries) {
            mLocalDataSource.saveDelivery(delivery);
        }
    }

    @Override
    public void onDeliveryLoadFailed() {

    }

    // GOODS
    public LiveData<List<Good>> loadGoods(int deliveryId) {
        return mLocalDataSource.loadGoods(deliveryId);
    }

    // REASONS
    public LiveData<List<Reason>> getReasons(){
        if(mReasons == null){
            loadReasonsFromNetwork();
            mReasons = mLocalDataSource.getReasons();
        }

        return mReasons;
    }

    private void loadReasonsFromNetwork(){
        mNetworkDataSource.loadReasons(this);
    }

    @Override
    public void onReasonsLoaded(List<Reason> reasons) {
        for (Reason reason:reasons) {
            mLocalDataSource.saveReason(reason);
        }
    }

    @Override
    public void onReasonsLoadFailed() {

    }

    // DEFECT
    public void saveDefect(Delivery delivery, Defect defect){
        //delivery.getDefectAct().addDefect(defect);
    }

    public LiveData<List<DefectGood>> getDefectGoods(Delivery delivery) {
        loadDeliveryGoodsFromNetwork(delivery);
        loadDefectsFromNetwork(delivery);
        return mLocalDataSource.getDefectGoods(delivery.getId());
    }

    private void loadDeliveryGoodsFromNetwork(Delivery delivery){
        mNetworkDataSource.loadGoods(delivery, this);
    }

    @Override
    public void onGoodsLoaded(List<Good> goods) {
        for (Good good:goods) {
            mLocalDataSource.saveGood(good);
        }
    }

    @Override
    public void onGoodsLoadFailed() {

    }

    private void loadDefectsFromNetwork(Delivery delivery){
        mNetworkDataSource.loadDefects(delivery, this);
    }

    @Override
    public void onDefectsLoaded(List<Defect> defects) {
        for (Defect defect:defects) {
            mLocalDataSource.saveDefect(defect);
        }
    }

    @Override
    public void onDefectsLoadFailed() {

    }

    public void getDefect(int defectId, LoadDefectCallback callback) {
        mLocalDataSource.getDefectById(defectId, callback);
    }

    public void getDefectReasons(int defectId, LoadDefectReasonsCallback callback) {
        mLocalDataSource.getDefectReasons(defectId, callback);
    }


}
