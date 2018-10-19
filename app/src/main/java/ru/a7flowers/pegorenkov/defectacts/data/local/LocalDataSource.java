package ru.a7flowers.pegorenkov.defectacts.data.local;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.AppExecutors;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.ClearDatabaseCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.SaveReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.GoodEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;
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

    public LiveData<List<Delivery>> getDeliveries() {
        Log.d(TAG, "Get all deliveries");
        return mDb.deliveryDao().loadAllDeliveries();
    }

    public LiveData<List<Reason>> getReasons() {
        Log.d(TAG, "Get all reasons");
        return mDb.reasonDao().loadReasons();
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

    public LiveData<List<GoodEntity>> loadGoodEntities(String[] deliveryIds) {
        Log.d(TAG, "Get delivery goods");
        return mDb.goodDao().loadGoodEntities(deliveryIds);
    }

    public LiveData<List<Good>> loadGoods(String[] deliveryIds) {
        Log.d(TAG, "Get delivery goods");
        return mDb.goodDao().loadGoods(deliveryIds);
    }

    public LiveData<List<DefectWithReasons>> getDefectGoods(String[] deliveryIds) {
        Log.d(TAG, "Get delivery defects");
        return mDb.defectDao().loadDefects(deliveryIds);
    }

    public LiveData<List<Diff>> getDiffGoods(String[] deliveryIds) {
        Log.d(TAG, "Get delivery diffs");
        return mDb.differenceDao().loadDifferencies(deliveryIds);
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

    public void saveDefects(final List<Defect> defects) {
        Log.d(TAG, "Insert defects");
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.defectDao().insertDefects(defects);
            }
        });
    }

    public void saveDefect(final Defect defect) {
        Log.d(TAG, "Insert 1 defect");
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.defectDao().insertDefect(defect);
            }
        });
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

    public void saveDefectReasons(final String defectId, final List<DefectReason> reasons, final SaveReasonsCallback callback) {
        Log.d(TAG, "Insert defect reasons");
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.defectReasonDao().deleteReasons(defectId);
                mDb.defectReasonDao().insertReasons(reasons);
                callback.onReasonsSaved();
            }
        });
    }

    public void deleteAll(final ClearDatabaseCallback callback) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.deliveryDao().deleteAllDeliveries();
                mDb.reasonDao().deleteAll();
                callback.onDatabaseCleared();
            }
        });
    }

    public void saveDefectsServer(final List<DefectWithReasons> defects) {
        if(defects == null) return;

        for (DefectWithReasons defectServer:defects) {
            saveDefectServer(defectServer);
        }
    }

    public void saveDefectServer(final DefectWithReasons defectServer) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                Defect defect = new Defect(defectServer);
                mDb.defectDao().insertDefect(defect);

                List<DefectReason> list = defectServer.getReasons();
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
                DefectWithReasons defect = mDb.defectDao().loadDefect(defectId);
                callback.onDefectLoaded(defect);
            }
        });
    }

    //DIFF
    public void getDiff(final String diffId, final DataSource.LoadDiffCallback callback) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                Diff diff = mDb.differenceDao().loadDifference(diffId);
                callback.onDiffLoaded(diff);
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

}
