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
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBudgeonAmountEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBulkEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueDiameterEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueLengthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueWeigthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.local.LocalDataSource;
import ru.a7flowers.pegorenkov.defectacts.data.network.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;
import ru.a7flowers.pegorenkov.defectacts.data.network.NetworkDataSource;

public class Repository {

    private volatile static Repository INSTANCE = null;

    private NetworkDataSource mNetworkDataSource;
    private LocalDataSource mLocalDataSource;

    private LiveData<List<User>> mUsers;
    private LiveData<List<Delivery>> mDeliveries;
    private LiveData<List<Reason>> mReasons;

    private Mode mMode = Mode.DEFECTS;
    private User mCurrentUser;

    //SAVED STATES
    private DefectData mDefectData;
    private DiffData mDiffData;

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
                loadUsersFromNetwork();
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

    //USERS
    public LiveData<List<User>> getUsers(){
        if(mUsers == null){
            mUsers = mLocalDataSource.getUsers();
        }
        return mUsers;
    }

    public void setCurrentUser(User user) {
        this.mCurrentUser = user;

        if(mCurrentUser.isDefectAccess() && !mCurrentUser.isDiffAccess()){
            mMode = Mode.DEFECTS;
        }else if(!mCurrentUser.isDefectAccess() && mCurrentUser.isDiffAccess()){
            mMode = Mode.DIFFERENCIES;
        }
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    private void loadUsersFromNetwork(){
        mNetworkDataSource.loadUsers(new DataSource.LoadUsersCallback() {
            @Override
            public void onUsersLoaded(List<User> users) {
                mLocalDataSource.saveUsers(users);
            }

            @Override
            public void onUsersLoadFailed() {

            }
        });
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

    public void saveDeliveryPhoto(final String deliveryId, String photoPath) {
        //TODO optimize
        mNetworkDataSource.saveDeliveryPhoto(mCurrentUser, deliveryId, photoPath, new DataSource.UploadPhotosCallback() {
            @Override
            public void onPhotosUploaded() {
                mNetworkDataSource.loadDelivery(deliveryId, new DataSource.LoadDeliveryCallback() {
                    @Override
                    public void onDeliveryLoaded(Delivery delivery) {
                        mLocalDataSource.saveDelivery(delivery);
                    }

                    @Override
                    public void onDeliveryLoadFailed() {

                    }
                });
            }

            @Override
            public void onPhotosUploadingFailed() {

            }
        });
    }

    // GOODS
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

                        for (Float value:good.getDiameter()) {
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
                        for (Float value:good.getBulk()) {
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

    public void saveDefect(Defect defect, final List<String> photoPaths){

        mNetworkDataSource.saveDefect(mCurrentUser, defect, new DataSource.UploadDefectCallback() {
            @Override
            public void onDefectUploaded(final Defect defect) {
                mNetworkDataSource.saveDefectPhotos(mCurrentUser, defect.getDeliveryId(), defect.getId(), photoPaths, new DataSource.UploadPhotosCallback() {
                    @Override
                    public void onPhotosUploaded() {
                        refreshDataAfterSavingDefect(defect);
                    }

                    @Override
                    public void onPhotosUploadingFailed() {
                        refreshDataAfterSavingDefect(defect);
                    }
                });
            }

            @Override
            public void onDefectUploadingFailed() {

            }
        });
    }

    public LiveData<List<Defect>> getDefectGoods(String[] deliveryIds) {
        loadGoodsFromNetwork(deliveryIds);
        loadDefectsFromNetwork(deliveryIds);
        return mLocalDataSource.getDefectGoods(deliveryIds);
    }

    private void loadDefectsFromNetwork(String[] deliveryIds){
        for (String deliveryId : deliveryIds) {
            mNetworkDataSource.loadDefects(deliveryId, new DataSource.LoadDefectsCallback() {
                @Override
                public void onDefectsLoaded(List<Defect> defects) {
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

    private void refreshDataAfterSavingDefect(Defect defect){
        mLocalDataSource.setDefectActExists(defect.getDeliveryId());
        mLocalDataSource.saveDefectServer(defect);
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
            mNetworkDataSource.loadDiff(deliveryId, new DataSource.LoadDiffsCallback() {
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

    public void saveDiff(Diff diff, final ArrayList<String> photoPaths) {
        mNetworkDataSource.saveDiff(mCurrentUser, diff, new DataSource.UploadDiffCallback() {
            @Override
            public void onDiffUploaded(final Diff diff) {
                mNetworkDataSource.saveDiffPhotos(mCurrentUser, diff.getDeliveryId(), diff.getId(), photoPaths, new DataSource.UploadPhotosCallback() {
                    @Override
                    public void onPhotosUploaded() {
                        refreshDataAfterSavingDiff(diff);
                    }

                    @Override
                    public void onPhotosUploadingFailed() {
                        refreshDataAfterSavingDiff(diff);
                    }
                });
            }

            @Override
            public void onDiffUploadingFailed() {

            }
        });
    }

    private void refreshDataAfterSavingDiff(Diff diff){
        mLocalDataSource.setDiffActExists(diff.getDeliveryId());
        mLocalDataSource.saveDiff(diff);
    }

    //SAVE STATE
    public void saveDefectData(DefectData defectData){
        mDefectData = defectData;
    }

    public DefectData getSavedDefectData() {
        return mDefectData;
    }

    public DiffData getSavedDiffData() {
        return mDiffData;
    }

    public void saveDiffData(DiffData mDiffData) {
        this.mDiffData = mDiffData;
    }


    public static class DefectData{
        private LiveData<List<Good>> mGoods;
        private Defect mDefect;
        private List<String> photoPaths = new ArrayList<>();

        public DefectData() {
        }

        public LiveData<List<Good>> getmGoods() {
            return mGoods;
        }

        public void setmGoods(LiveData<List<Good>> mGoods) {
            this.mGoods = mGoods;
        }

        public Defect getmDefect() {
            return mDefect;
        }

        public void setmDefect(Defect mDefect) {
            this.mDefect = mDefect;
        }

        public List<String> getPhotoPaths() {
            return photoPaths;
        }

        public void setPhotoPaths(List<String> photoPaths) {
            this.photoPaths = photoPaths;
        }
    }

    public static class DiffData{
        private LiveData<List<Good>> mGoods;
        private Diff mDiff;
        private Good mDiffGood;
        private List<String> photoPaths = new ArrayList<>();

        public LiveData<List<Good>> getmGoods() {
            return mGoods;
        }

        public void setmGoods(LiveData<List<Good>> mGoods) {
            this.mGoods = mGoods;
        }

        public List<String> getPhotoPaths() {
            return photoPaths;
        }

        public void setPhotoPaths(List<String> photoPaths) {
            this.photoPaths = photoPaths;
        }

        public Diff getmDiff() {
            return mDiff;
        }

        public void setmDiff(Diff mDiff) {
            this.mDiff = mDiff;
        }

        public Good getmDiffGood() {
            return mDiffGood;
        }

        public void setmDiffGood(Good mDiffGood) {
            this.mDiffGood = mDiffGood;
        }
    }


    //TODO create adapter entity to value

    //TODO add current user
    //TODO add user to all queries
    //TODO set mode depends on user
}
