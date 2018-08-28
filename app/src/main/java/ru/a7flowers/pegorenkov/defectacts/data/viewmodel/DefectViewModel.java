package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class DefectViewModel extends AndroidViewModel implements DataSource.LoadReasonsCallback {

    private Repository mRepository;
    // General
    private Delivery mDelivery;
    private LiveData<List<Good>> mGoods;

    // For defect
    private Defect mDefect;
    private MutableLiveData<Integer> mDefectAmount = new MutableLiveData<>();
    private MutableLiveData<String> mDefectComment = new MutableLiveData<>();
    private MutableLiveData<String> mDefectSeries = new MutableLiveData<>();
    private MutableLiveData<List<Reason>> mDefectReasons = new MutableLiveData<>();
    private List<String> photoPaths = new ArrayList<>();

    private String currentPhotoPath;

    public DefectViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;

        init();
    }

    private void init(){
        mDefect = new Defect();

        mDefectComment.postValue("");
        mDefectAmount.postValue(0);
        mDefectSeries.postValue("");
        mDefectReasons.postValue(new ArrayList<Reason>());
    }

    public void start(Delivery delivery){
        loadDelivery(delivery);
    }

    public void start(Delivery delivery, Defect defect){
        loadDelivery(delivery);

        mDefect = defect;

        mDefectComment.postValue(mDefect.getComment());
        mDefectAmount.postValue(mDefect.getQuantity());

        mRepository.getDefectReasons(mDelivery, mDefect, this);
    }

    private void loadDelivery(Delivery delivery){
        mDelivery = delivery;
        mGoods = mRepository.loadGoods(delivery.getId());
    }

    public LiveData<List<Good>> getGoods() {
        return mGoods;
    }

    public LiveData<List<Reason>> getDefectReasons() {
        return mDefectReasons;
    }

    public String getCurrentSeries(){
        return mDefectSeries.getValue();
    }

    public void incAmount(){
        int value = mDefectAmount.getValue();
        value++;
        mDefectAmount.postValue(value);
    }

    public void decAmount(){
        int value = mDefectAmount.getValue();
        value = value == 0 ? 0 : value-1;
        mDefectAmount.postValue(value);
    }

    public MutableLiveData<Integer> getDefectAmount() {
        return mDefectAmount;
    }

    public MutableLiveData<String> getDefectComment() {
        return mDefectComment;
    }

    public MutableLiveData<String> getDefectSeries() {
        return mDefectSeries;
    }

    public void setGood(Good good){
        mDefectSeries.postValue(good.getSeries());
    }

    public void setSeries(String series){
        mDefectSeries.postValue(series);
    }

    public void setAmount(int value){
        if(mDefectAmount.getValue() != value)
            mDefectAmount.postValue(value);
    }

    public void setComment(String text){
        if(!mDefectComment.getValue().equals(text))
            mDefectComment.postValue(text);
    }

    public void setDefectReasons(List<Reason> defectReasons) {
        mDefectReasons.postValue(defectReasons);
    }

    public void saveDefect(){
        mDefect.setQuantity(mDefectAmount.getValue());
        mDefect.setSeries(mDefectSeries.getValue());
        mDefect.setComment(mDefectComment.getValue());
        mDefect.setDeliveryId(mDelivery.getId());

        mRepository.saveDefect(mDelivery, mDefect, mDefectReasons.getValue(), photoPaths);

        init();
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    public List<Reason> getDefectReasonsList(){
        return mDefectReasons.getValue();
    }

    @Override
    public void onReasonsLoaded(List<Reason> reasons) {
        if(reasons != null)
            mDefectReasons.postValue(reasons);
    }

    @Override
    public void onReasonsLoadFailed() {

    }

    public void savePhoto() {
        photoPaths.add(currentPhotoPath);
    }
}
