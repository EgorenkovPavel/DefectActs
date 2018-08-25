package ru.a7flowers.pegorenkov.defectacts.data;

import android.support.annotation.NonNull;

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
}
