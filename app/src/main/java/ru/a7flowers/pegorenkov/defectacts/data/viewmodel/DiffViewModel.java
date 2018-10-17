package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;

public class DiffViewModel extends AndroidViewModel {

    private Repository mRepository;

    public DiffViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;
    }

    public void start(String[] deliveryIds){

    }

    public void start(String[] deliveryIds, String diffId){

    }

    public void setGood(Good good) {
        //this.good = good;
    }

    public void setAmount(int amount) {
//        this.amount = amount;
    }

    public void setComment(String comment) {
//        this.comment = comment;
    }

    public void incAmount() {
    }

    public void decAmount() {
    }

    public void saveDiff() {
    }

    public LiveData<List<Good>> getGoods() {
        return null;
    }

    public LiveData<Integer> getDiffAmount() {
        return null;
    }

    public LiveData<String> getDiffComment() {
        return null;
    }

    public LiveData<String> getDiffSeries() {
        return null;
    }

    public LiveData<String> getDiffTitle() {
        return null;
    }

    public LiveData<String> getDiffSuplier() {
        return null;
    }

    public LiveData<String> getDiffCountry() {
        return null;
    }

    public LiveData<String> getDiffDelivery() {
        return null;
    }

    public void setPhotoPath(String photoPath) {
//        this.photoPath = photoPath;
    }
}
