package ru.a7flowers.pegorenkov.defectacts.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;

public class Repository {

    private volatile static Repository INSTANCE = null;

    private NetworkDataSource mNetworkDataSource;
    private LocalDataSource mLocalDataSource;

    private LiveData<List<Delivery>> mDeliveries;
    private LiveData<List<Reason>> mReasons;

    private Repository(NetworkDataSource networkDataSource, LocalDataSource localDataSource){
        mNetworkDataSource = networkDataSource;
        mLocalDataSource = localDataSource;

        mLocalDataSource.deleteAll(new DataSource.ClearDatabaseCallback() {
            @Override
            public void onDatabaseCleared() {
                loadDeliveriesFromNetwork();
                loadReasonsFromNetwork();
            }

            @Override
            public void onDatabaseClearingFailed() {

            }
        });
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
            mDeliveries = mLocalDataSource.getDeliveries();
        }
        return mDeliveries;
    }

    private void loadDeliveriesFromNetwork(){
        mNetworkDataSource.loadDeliveries(new DataSource.LoadDeliveriesCallback() {
            @Override
            public void onDeliveriesLoaded(List<Delivery> deliveries) {
                mLocalDataSource.saveDeliveries(deliveries);
            }

            @Override
            public void onDeliveriesLoadFailed() {

            }
        });
    }

    // GOODS
    public LiveData<List<Good>> loadGoods(String[] deliveryIds) {
        return mLocalDataSource.loadGoods(deliveryIds);
    }

    private void loadGoodsFromNetwork(String[] deliveryIds){
        for (int i = 0; i<deliveryIds.length; i++) {
            String deliveryId = deliveryIds[i];
            mNetworkDataSource.loadGoods(deliveryId, new DataSource.LoadGoodsCallback() {
                @Override
                public void onGoodsLoaded(List<Good> goods) {
                    mLocalDataSource.saveGoods(goods);
                }

                @Override
                public void onGoodsLoadFailed() {

                }
            });
        }
    }

    // REASONS
    public LiveData<List<Reason>> getReasons(){
        if(mReasons == null){
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
    public void saveDefect(DefectWithReasons defect, final List<String> photoPaths){

        mNetworkDataSource.saveDefectWithReasons(defect, new DataSource.UploadDefectCallback() {
            @Override
            public void onDefectUploaded(final DefectWithReasons defect) {
                mNetworkDataSource.savePhotos(defect.getDeliveryId(), defect.getId(), photoPaths, new DataSource.UploadPhotosCallback() {
                    @Override
                    public void onPhotosUploaded() {
                        refreshDataAfterSaving(defect);
                    }

                    @Override
                    public void onPhotosUploadingFailed() {

                    }
                });
            }

            @Override
            public void onDefectUploadingFailed() {

            }
        });
    }

    public LiveData<List<DefectWithReasons>> getDefectGoods(String[] deliveryIds) {
        loadGoodsFromNetwork(deliveryIds);
        loadDefectsFromNetwork(deliveryIds);
        return mLocalDataSource.getDefectGoods(deliveryIds);
    }

    private void loadDefectsFromNetwork(String[] deliveryIds){
        for (int i = 0; i<deliveryIds.length; i++) {
            String deliveryId = deliveryIds[i];
            mNetworkDataSource.loadDefectsWithReasons(deliveryId, new DataSource.LoadDefectsCallback() {
                @Override
                public void onDefectsLoaded(List<DefectWithReasons> defects) {
                    mLocalDataSource.saveDefectsServer(defects);
                }

                @Override
                public void onDefectsLoadFailed() {

                }
            });
        }
    }

    public void getDefectReasons(String[] deliveryIds, final Defect defect, final LoadReasonsCallback callback) {
        mLocalDataSource.getDefectReasons(defect.getId(), callback);
    }

    private void refreshDataAfterSaving(DefectWithReasons defect){
        mNetworkDataSource.loadDelivery(defect.getDeliveryId(), new DataSource.LoadDeliveryCallback() {
            @Override
            public void onDeliveryLoaded(Delivery delivery) {
                mLocalDataSource.saveDelivery(delivery);
            }

            @Override
            public void onDeliveryLoadFailed() {

            }
        });

        mNetworkDataSource.loadDefectWithReasons(defect.getDeliveryId(), defect.getId(), new DataSource.LoadDefectCallback() {
            @Override
            public void onDefectLoaded(DefectWithReasons defect) {
                mLocalDataSource.saveDefectServer(defect);
            }

            @Override
            public void onDefectLoadFailed() {

            }
        });
    }

}
