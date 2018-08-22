package ru.a7flowers.pegorenkov.defectacts.data;

import android.support.annotation.NonNull;

public class NetworkDataSource implements DataSource {

    private static volatile NetworkDataSource INSTANCE;

    //private AppDatabase mDb;
    private AppExecutors mAppExecutors;

    private NetworkDataSource() {
        mAppExecutors = AppExecutors.getInstance();
        //mDb = db;
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

}
