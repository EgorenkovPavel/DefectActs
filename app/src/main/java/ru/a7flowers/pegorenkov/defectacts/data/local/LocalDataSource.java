package ru.a7flowers.pegorenkov.defectacts.data.local;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.AppExecutors;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.ClearDatabaseCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReasonEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DifferenceEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.GoodEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.UploadPhotoEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBudgeonAmountEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBulkEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueDiameterEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueLengthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueWeigthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.network.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

public class LocalDataSource {

    private static final String TAG = "DATA_TRANSFER";
    private static volatile LocalDataSource INSTANCE;

    private AppDatabase mDb;
    private AppExecutors mAppExecutors;

    private LocalDataSource(@NonNull AppDatabase db) {
        mAppExecutors = AppExecutors.getInstance();
        mDb = db;
    }

    public static LocalDataSource getInstance(@NonNull AppDatabase db) {
        if (INSTANCE == null) {
            synchronized (LocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalDataSource(db);
                }
            }
        }
        return INSTANCE;
    }

    public void deleteAll(final ClearDatabaseCallback callback) {

        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.userDao().deleteAllUsers();
                mDb.deliveryDao().deleteAllDeliveries();
                mDb.reasonDao().deleteAll();
                mDb.goodDao().deleteAllBudgeonsAmounts();
                mDb.goodDao().deleteAllBulks();
                mDb.goodDao().deleteAllDiameters();
                mDb.goodDao().deleteAllLengths();
                mDb.goodDao().deleteAllWeigths();
                callback.onDatabaseCleared();
            }
        });
    }

    //USERS
    public LiveData<List<User>> getUsers() {
        Log.d(TAG, "Get all users");
        return mDb.userDao().loadAllUsers();
    }

    public void saveUsers(final List<User> users) {
        Log.d(TAG, "Insert users");
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.userDao().insertUsers(users);
            }
        });
    }

    //REASONS
    public LiveData<List<Reason>> getReasons() {
        Log.d(TAG, "Get all reasons");
        return mDb.reasonDao().loadReasons();
    }

    public void saveReasons(final List<Reason> reasons) {
        Log.d(TAG, "Insert reasons");
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.reasonDao().insertReasons(reasons);
            }
        });
    }

    //DELIVERIES
    public LiveData<List<Delivery>> getDeliveries() {
        Log.d(TAG, "Get all deliveries");
        return mDb.deliveryDao().loadAllDeliveries();
    }

    public void saveDeliveries(final List<Delivery> deliveries){
        Log.d(TAG, "Insert all delivery");
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.deliveryDao().insertDeliveries(deliveries);
            }
        });
    }

    public void saveDelivery(final Delivery delivery) {
        Log.d(TAG, "Insert 1 delivery");
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.deliveryDao().insertDelivery(delivery);
            }
        });
    }

    public void setDefectActExists(final String deliveryId) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.deliveryDao().setDefectActExist(deliveryId);
            }
        });
    }

    public void setDiffActExists(final String deliveryId) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.deliveryDao().setDiffActExist(deliveryId);
            }
        });
    }

    public void setDeliveryPhotoCount(final String deliveryId, final int photoCount) {
        mAppExecutors.discIO().execute(() -> mDb.deliveryDao().setPhotoCount(deliveryId, photoCount));
    }

    //GOODS
    public LiveData<List<Good>> loadGoods(String[] deliveryIds) {
        Log.d(TAG, "Get delivery goods");
        return mDb.goodDao().loadGoods(deliveryIds);
    }

    public void saveGoods(final List<GoodEntity> goods) {
        if(goods == null) return;

        Log.d(TAG, "Insert goods");
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.goodDao().insertGoods(goods);
            }
        });
    }

    //DEFECTS
    public LiveData<List<Defect>> getDefectGoods(String[] deliveryIds) {
        Log.d(TAG, "Get delivery defects");
        return mDb.defectDao().loadDefects(deliveryIds);
    }

    public void getDefectReasons(final String defectId, final LoadReasonsCallback callback) {
        Log.d(TAG, "Get delivery defect reasons");
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Reason> reasons = mDb.defectReasonDao().loadReasons(defectId);
                callback.onReasonsLoaded(reasons);
            }
        });
    }

    public void saveDefectsServer(final List<Defect> defects) {
        if(defects == null) return;

        for (Defect defectServer:defects) {
            saveDefectServer(defectServer);
        }
    }

    public void saveDefectServer(final Defect defectServer) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                DefectEntity defect = new DefectEntity(defectServer);
                mDb.defectDao().insertDefect(defect);

                List<DefectReasonEntity> list = defectServer.getReasons();
                mDb.defectReasonDao().deleteReasons(defectServer.getId());
                if (list.isEmpty()) return;

                mDb.defectReasonDao().insertReasons(list);
            }
        });
    }

    public void getDefect(final String defectId, final LoadDefectCallback callback) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                Defect defect = mDb.defectDao().loadDefect(defectId);
                callback.onDefectLoaded(defect);
            }
        });
    }

    public void setDefectPhotoCount(final String deliveryId, final String id, final int photoCount) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.defectDao().setPhotoCount(deliveryId, id, photoCount);
            }
        });
    }

    public void getDefectsByGood(final Good good, final String exeptedDefectId, final DataSource.LoadDefectsCallback callback) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Defect> defects = mDb.defectDao().loadDefectsByGood(good.getDeliveryId(), good.getSeries(), exeptedDefectId);
                callback.onDefectsLoaded(defects);
            }
        });
    }

    //DIFF
    public LiveData<List<Diff>> getDiffGoods(String[] deliveryIds) {
        Log.d(TAG, "Get delivery diffs");
        return mDb.differenceDao().loadDifferencies(deliveryIds);
    }

    public void getDiff(final String diffId, final DataSource.LoadDiffCallback callback) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                Diff diff = mDb.differenceDao().loadDifference(diffId);
                callback.onDiffLoaded(diff);
            }
        });
    }

    public void saveDiffs(final List<Diff> diffs){
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                List<DifferenceEntity> entities = new ArrayList<>();
                for (Diff diff:diffs) {
                    entities.add(new DifferenceEntity(diff));
                }
                mDb.differenceDao().insertDifferencies(entities);
            }
        });
    }

    public void getGood(final String deliveryId, final String series, final DataSource.LoadGoodCallback callback) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                Good good = mDb.goodDao().loadGood(deliveryId, series);
                callback.onGoodLoaded(good);
            }
        });
    }

    public void saveDiff(final Diff diff) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                DifferenceEntity differenceEntity = new DifferenceEntity(diff);
                mDb.differenceDao().insertDifference(differenceEntity);
            }
        });
    }

    public void setDiffPhotoCount(final String deliveryId, final String id, final int photoCount) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.differenceDao().setPhotoCount(deliveryId, id, photoCount);
            }
        });
    }

    public void getDiffsByGood(final Good good, final String exeptedDiffId, final DataSource.LoadDiffsCallback callback) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Diff> diffs = mDb.differenceDao().loadDiffsByGood(good.getDeliveryId(), good.getSeries(), exeptedDiffId);
                callback.onDiffsLoaded(diffs);
            }
        });
    }

    //VALUES
    public void saveDiameters(final List<ValueDiameterEntity> diameters) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.goodDao().insertDiameters(diameters);
            }
        });
    }

    public void saveLengths(final List<ValueLengthEntity> lengths) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.goodDao().insertLengths(lengths);
            }
        });
    }

    public void saveWeigths(final List<ValueWeigthEntity> weigths) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.goodDao().insertWeigths(weigths);
            }
        });
    }

    public void saveBudgeonAmounts(final List<ValueBudgeonAmountEntity> budgeonAmounts) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.goodDao().insertBudgeonAmounts(budgeonAmounts);
            }
        });
    }

    public void saveBulks(final List<ValueBulkEntity> bulks) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.goodDao().insertBulk(bulks);
            }
        });
    }

    //UPLOAD PHOTOS
    public void saveUploadPhotos(final List<UploadPhotoEntity> entities, DataSource.SavePhotoCallback callback){
        mAppExecutors.discIO().execute(() -> {
            mDb.uploadPhotoDao().insert(entities);
            callback.onPhotoSaved();
        });
    }

    public List<UploadPhotoEntity> getUploadPhotos(){
        return mDb.uploadPhotoDao().getUploadPhotos(UploadPhotoEntity.MAX_TRY_NUMBER);
    }

    public void updateUploadPhoto(UploadPhotoEntity entity){
        mDb.uploadPhotoDao().updatePhoto(entity);
    }

    public void deleteUploadPhoto(UploadPhotoEntity entity){
        mDb.uploadPhotoDao().deletePhoto(entity);
    }

    public LiveData<List<UploadPhotoEntity>> getFailedUploadPhotos() {
        return mDb.uploadPhotoDao().getFailedUploadPhotos(UploadPhotoEntity.MAX_TRY_NUMBER);
    }

    public LiveData<List<UploadPhotoEntity>> getAllUploadPhotos() {
        return mDb.uploadPhotoDao().getAllUploadPhotos();
    }

    public void clearUploadPhotos(DataSource.SavePhotoCallback callback){
        mAppExecutors.discIO().execute(() -> {
            mDb.uploadPhotoDao().clearPhotos(UploadPhotoEntity.MAX_TRY_NUMBER);
            callback.onPhotoSaved();
        });
    }

    public void updateUploadPhotos(List<UploadPhotoEntity> entities, DataSource.SavePhotoCallback callback) {
        mAppExecutors.discIO().execute(() -> {
            mDb.uploadPhotoDao().updatePhotos(entities);
            callback.onPhotoSaved();
        });
    }

    public void deleteAllUploadPhotos(DataSource.SavePhotoCallback callback) {
        mAppExecutors.discIO().execute(() -> {
            mDb.uploadPhotoDao().deleteAllPhotos();
            callback.onPhotoSaved();
        });
    }
}
