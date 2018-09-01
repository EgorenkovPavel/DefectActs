package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;

public class DefectViewModel extends AndroidViewModel{

    private Repository mRepository;
    // General
    private String[] mDeliveryIds;
    private LiveData<List<Good>> mGoods;

    // For defect
    private String mDefectId;
    private String mDefectDeliveryId;
    private MutableLiveData<String> mDefectTitle = new MutableLiveData<>();
    private MutableLiveData<String> mDefectSuplier = new MutableLiveData<>();
    private MutableLiveData<String> mDefectCountry = new MutableLiveData<>();
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
        mDefectId = "";
        mDefectTitle.postValue("");
        mDefectSuplier.postValue("");
        mDefectCountry.postValue("");
        mDefectComment.postValue("");
        mDefectAmount.postValue(0);
        mDefectSeries.postValue("");
        mDefectReasons.postValue(new ArrayList<Reason>());
    }

    public void start(String[] deliveryIds){
        loadDelivery(deliveryIds);
    }

    public void start(String[] deliveryIds, String defectId){
        loadDelivery(deliveryIds);

        mDefectId = defectId;

        mRepository.getDefect(defectId, new DataSource.LoadDefectCallback() {
            @Override
            public void onDefectLoaded(DefectWithReasons defect) {
                mDefectTitle.postValue(defect.getTitle());
                mDefectSuplier.postValue(defect.getSuplier());
                mDefectCountry.postValue(defect.getCountry());
                mDefectDeliveryId = defect.getDeliveryId();
                mDefectComment.postValue(defect.getComment());
                mDefectAmount.postValue(defect.getQuantity());
            }

            @Override
            public void onDefectLoadFailed() {

            }
        });

        mRepository.getDefectReasons(defectId, new DataSource.LoadReasonsCallback() {
            @Override
            public void onReasonsLoaded(List<Reason> reasons) {
                if(reasons != null)
                    mDefectReasons.postValue(reasons);
            }

            @Override
            public void onReasonsLoadFailed() {

            }
        });
    }

    private void loadDelivery(String[] deliveryIds){
        mDeliveryIds = deliveryIds;
        mGoods = mRepository.loadGoods(deliveryIds);
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
        mDefectDeliveryId = good.getDeliveryId();
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
        Defect defect = new Defect();
        defect.setId(mDefectId);
        defect.setQuantity(mDefectAmount.getValue());
        defect.setSeries(mDefectSeries.getValue());
        defect.setComment(mDefectComment.getValue());
        defect.setDeliveryId(mDefectDeliveryId);

        DefectWithReasons defectWithReasons = new DefectWithReasons(defect, mDefectReasons.getValue());

        mRepository.saveDefect(defectWithReasons,
                new ArrayList<>(photoPaths));

        init();
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    public List<Reason> getDefectReasonsList(){
        return mDefectReasons.getValue();
    }

    public void savePhoto() {
        photoPaths.add(currentPhotoPath);
    }
}
