package ru.a7flowers.pegorenkov.defectacts.data;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

public class Repository {

    private volatile static Repository INSTANCE = null;

    private NetworkDataSource mNetworkDataSource;
    private LocalDataSource mLocalDataSource;

    private LiveData<List<Delivery>> mDeliveries;
    private LiveData<List<Reason>> mReasons;

    private Repository(NetworkDataSource networkDataSource, LocalDataSource localDataSource){
        mNetworkDataSource = networkDataSource;
        mLocalDataSource = localDataSource;

        getReasons();
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
        mNetworkDataSource.loadDeliveries(new DataSource.LoadDeliveriesCallback() {
            @Override
            public void onDeliveriesLoaded(List<Delivery> deliveries) {
                for (Delivery delivery:deliveries) {
                    mLocalDataSource.saveDelivery(delivery);
                }
            }

            @Override
            public void onDeliveryLoadFailed() {

            }
        });
    }

    // GOODS
    public LiveData<List<Good>> loadGoods(String deliveryId) {
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
        mNetworkDataSource.loadReasons(new LoadReasonsCallback() {
            @Override
            public void onReasonsLoaded(List<Reason> reasons) {
                mLocalDataSource.saveReasons(reasons);
            }

            @Override
            public void onReasonsLoadFailed() {

            }
        });
    }

    // DEFECT
    public void saveDefect(Delivery delivery, Defect defect, List<Reason> reasons, List<String> photoPaths){
        if (!delivery.isActExist()){
            mNetworkDataSource.createAct(delivery);
            delivery.setActExist(true);
            mLocalDataSource.saveDelivery(delivery);
        }
        mNetworkDataSource.saveDefect(defect, reasons);
        mNetworkDataSource.savePhotos(photoPaths);

        mLocalDataSource.saveDefect(defect);
        for (Reason reason:reasons) {
            mLocalDataSource.saveDefectReason(new DefectReason(defect.getId(), reason.getId()));
        }


        //delivery.getDefectAct().addDefect(defect);
    }

    public LiveData<List<DefectGood>> getDefectGoods(Delivery delivery) {
        loadDeliveryGoodsFromNetwork(delivery);
        loadDefectsFromNetwork(delivery);
        return mLocalDataSource.getDefectGoods(delivery.getId());
    }

    private void loadDeliveryGoodsFromNetwork(Delivery delivery){
        mNetworkDataSource.loadGoods(delivery, new DataSource.LoadGoodsCallback() {
            @Override
            public void onGoodsLoaded(List<Good> goods) {
                mLocalDataSource.saveGoods(goods);
            }

            @Override
            public void onGoodsLoadFailed() {

            }
        });
    }

    private void loadDefectsFromNetwork(Delivery delivery){
        mNetworkDataSource.loadDefects(delivery, new DataSource.LoadDefectsCallback() {
            @Override
            public void onDefectsLoaded(List<Defect> defects) {
                mLocalDataSource.saveDefects(defects);
            }

            @Override
            public void onDefectsLoadFailed() {

            }
        });
    }

    public void getDefectReasons(Delivery delivery, final Defect defect, final LoadReasonsCallback callback) {
        mNetworkDataSource.loadDefectReasons(delivery, defect, new LoadDefectReasonsCallback() {
            @Override
            public void onDefectReasonsLoaded(List<DefectReason> reasons) {
                mLocalDataSource.saveDefectReasons(reasons, new DataSource.SaveReasonsCallback() {
                    @Override
                    public void onReasonsSaved() {
                        mLocalDataSource.getDefectReasons(defect.getId(), callback);
                    }

                    @Override
                    public void onReasonsSavingFailed() {

                    }
                });
            }

            @Override
            public void onDefectReasonsLoadFailed() {

            }
        });
    }

}
