package ru.a7flowers.pegorenkov.defectacts.data;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor discIO;
    private final Executor networkIO;
    private final Executor mainThread;

    private AppExecutors(Executor discIO, Executor networkIO, Executor mainThread){
        this.discIO = discIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;

    }

    public static AppExecutors getInstance(){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor discIO() {
        return discIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor{
        private Handler mainThreadHeandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHeandler.post(runnable);
        }
    }

}
