package ru.a7flowers.pegorenkov.defectacts.data;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.ReloadDataCallback;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.GoodEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBudgeonAmountEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBulkEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueDiameterEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueLengthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueWeigthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.local.LocalDataSource;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;
import ru.a7flowers.pegorenkov.defectacts.data.network.NetworkDataSource;

public class Repository {

    private volatile static Repository INSTANCE = null;

    private NetworkDataSource mNetworkDataSource;
    private LocalDataSource mLocalDataSource;

    private LiveData<List<Delivery>> mDeliveries;
    private LiveData<List<Reason>> mReasons;
    private Mode mMode = Mode.DEFECTS;

    private Repository(NetworkDataSource networkDataSource, LocalDataSource localDataSource){
        mNetworkDataSource = networkDataSource;
        mLocalDataSource = localDataSource;

        reloadData(null);
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

    public void reloadData(final ReloadDataCallback callback){
        mLocalDataSource.deleteAll(new DataSource.ClearDatabaseCallback() {
            @Override
            public void onDatabaseCleared() {
                loadDeliveriesFromNetwork(callback);
                loadReasonsFromNetwork();
            }

            @Override
            public void onDatabaseClearingFailed() {
                if(callback != null) callback.onDataReloadingFailed();
            }
        });
    }

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        this.mMode = mode;
    }

    // DELIVERY
    public LiveData<List<Delivery>> getDeliveries(){
        if(mDeliveries == null){
            mDeliveries = mLocalDataSource.getDeliveries();
        }
        return mDeliveries;
    }

    private void loadDeliveriesFromNetwork(final ReloadDataCallback callback){
        mNetworkDataSource.loadDeliveries(new DataSource.LoadDeliveriesCallback() {
            @Override
            public void onDeliveriesLoaded(List<Delivery> deliveries) {
                mLocalDataSource.saveDeliveries(deliveries);
                if(callback != null) callback.onDataReloaded();
            }

            @Override
            public void onDeliveriesLoadFailed() {

            }
        });
    }

    // GOODS
    public LiveData<List<GoodEntity>> loadGoodEntities(String[] deliveryIds) {
        return mLocalDataSource.loadGoodEntities(deliveryIds);
    }

    public LiveData<List<Good>> loadGoods(String[] deliveryIds) {
        return mLocalDataSource.loadGoods(deliveryIds);
    }

    private void loadGoodsFromNetwork(String[] deliveryIds){
        for (String deliveryId : deliveryIds) {
            mNetworkDataSource.loadGoods(deliveryId, new DataSource.LoadGoodsCallback() {
                @Override
                public void onGoodsLoaded(List<Good> goods) {
                    List<GoodEntity> goodEntityList = new ArrayList<>();
                    List<ValueDiameterEntity> diameters = new ArrayList<>();
                    List<ValueLengthEntity> lengths = new ArrayList<>();
                    List<ValueWeigthEntity> weigths = new ArrayList<>();
                    List<ValueBudgeonAmountEntity> budgeonAmounts = new ArrayList<>();
                    List<ValueBulkEntity> bulks = new ArrayList<>();

                    for (Good good:goods) {
                        goodEntityList.add(new GoodEntity(good.getSeries(), good.getGood(),
                                good.getSuplier(), good.getCountry(), good.getDeliveryQuantity(),
                                good.getDeliveryId(), good.getDeliveryNumber()));

                        for (Integer value:good.getDiameter()) {
                            diameters.add(new ValueDiameterEntity(good.getSeries(), value));
                        }
                        for (Integer value:good.getLength()) {
                            lengths.add(new ValueLengthEntity(good.getSeries(), value));
                        }
                        for (Integer value:good.getWeigth()) {
                            weigths.add(new ValueWeigthEntity(good.getSeries(), value));
                        }
                        for (Integer value:good.getBudgeonAmount()) {
                            budgeonAmounts.add(new ValueBudgeonAmountEntity(good.getSeries(), value));
                        }
                        for (Integer value:good.getBulk()) {
                            bulks.add(new ValueBulkEntity(good.getSeries(), value));
                        }
                    }

                    mLocalDataSource.saveGoods(goodEntityList);
                    mLocalDataSource.saveDiameters(diameters);
                    mLocalDataSource.saveLengths(lengths);
                    mLocalDataSource.saveWeigths(weigths);
                    mLocalDataSource.saveBudgeonAmounts(budgeonAmounts);
                    mLocalDataSource.saveBulks(bulks);
                }

                @Override
                public void onGoodsLoadFailed() {

                }
            });
        }
    }

    public void getGood(String deliveryId, String series, DataSource.LoadGoodCallback callback) {
        mLocalDataSource.getGood(deliveryId, series, callback);
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
    public void getDefect(String defectId, LoadDefectCallback callback){
        mLocalDataSource.getDefect(defectId, callback);
    }

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
                        refreshDataAfterSaving(defect);
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
        for (String deliveryId : deliveryIds) {
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

    public void getDefectReasons(String defectId, final LoadReasonsCallback callback) {
        mLocalDataSource.getDefectReasons(defectId, callback);
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

        mNetworkDataSource.loadDefectWithReasons(defect.getDeliveryId(), defect.getId(), new LoadDefectCallback() {
            @Override
            public void onDefectLoaded(DefectWithReasons defect) {
                mLocalDataSource.saveDefectServer(defect);
            }

            @Override
            public void onDefectLoadFailed() {

            }
        });
    }

    //DIFF
    public void getDiff(String diffId, DataSource.LoadDiffCallback callback){
        mLocalDataSource.getDiff(diffId, callback);
    }

    public LiveData<List<Diff>> getDiffGoods(String[] deliveryIds) {
        loadGoodsFromNetwork(deliveryIds);
        loadDiffsFromNetwork(deliveryIds);
        return mLocalDataSource.getDiffGoods(deliveryIds);
    }

    private void loadDiffsFromNetwork(String[] deliveryIds){
        for (String deliveryId : deliveryIds) {
            mNetworkDataSource.loadDifferencies(deliveryId, new DataSource.LoadDiffsCallback() {
                @Override
                public void onDiffsLoaded(List<Diff> diffs) {
                    mLocalDataSource.saveDiffs(diffs);
                }

                @Override
                public void onDiffsLoadFailed() {

                }
            });
        }
    }

    public void saveDiff(Diff diff, ArrayList<String> strings) {
        //TODO
    }

    //TODO create adapter entity to value
}
