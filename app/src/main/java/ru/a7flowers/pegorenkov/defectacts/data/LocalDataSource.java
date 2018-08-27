package ru.a7flowers.pegorenkov.defectacts.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectCallback;
import ru.a7flowers.pegorenkov.defectacts.data.DataSource.LoadDefectReasonsCallback;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

public class LocalDataSource {

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
        return mDb.deliveryDao().loadAllDeliveries();
    }

    public LiveData<List<Reason>> getReasons() {
        return mDb.reasonDao().loadReasons();
    }

    public void saveDelivery(final Delivery delivery){
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.deliveryDao().insertDelivery(delivery);
            }
        });
    }

    public LiveData<List<Good>> loadGoods(int deliveryId) {
        return mDb.goodDao().loadGoods(deliveryId);
    }

    public void getDefectById(final int defectId, final LoadDefectCallback callback) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                Defect defect = mDb.defectDao().getDefectById(defectId);
                callback.onDefectLoaded(defect);
            }
        });
    }

    public LiveData<Delivery> getDelivery(int deliveryId) {
        return mDb.deliveryDao().getDeliveryById(deliveryId);
    }

    public LiveData<List<DefectGood>> getDefectGoods(int deliveryId) {
        return mDb.defectDao().loadDefects(deliveryId);
    }

    public void getDefectReasons(final int defectId, final LoadDefectReasonsCallback callback) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Reason> defects = mDb.defectReasonDao().loadReasons(defectId);
                callback.onDefectReasonsLoaded(defects);
            }
        });
    }

    public void saveGood(final Good good){
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.goodDao().insertGood(good);
            }
        });
    }

    public void saveDefect(final Defect defect){
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.defectDao().insertDefect(defect);
            }
        });
    }

    public void saveReason(final Reason reason) {
        mAppExecutors.discIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.reasonDao().insertReason(reason);
            }
        });
    }

}
