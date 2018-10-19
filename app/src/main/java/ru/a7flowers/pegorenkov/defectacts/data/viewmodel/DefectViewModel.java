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
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

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
    private MutableLiveData<String> mDefectDelivery = new MutableLiveData<>();
    private MutableLiveData<Integer> mDefectAmount = new MutableLiveData<>();
    private MutableLiveData<Integer> mDefectWriteoff = new MutableLiveData<>();
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
        mDefectDelivery.postValue("");
        mDefectComment.postValue("");
        mDefectAmount.postValue(0);
        mDefectWriteoff.postValue(0);
        mDefectSeries.postValue("");
        mDefectReasons.postValue(new ArrayList<Reason>());
        photoPaths = new ArrayList<>();
    }

    public void start(String[] deliveryIds){
        loadDelivery(deliveryIds);
    }

    public void start(String[] deliveryIds, final String defectId){
        loadDelivery(deliveryIds);

        mDefectId = defectId;

        mRepository.getDefect(defectId, new DataSource.LoadDefectCallback() {
            @Override
            public void onDefectLoaded(DefectWithReasons defect) {
                mDefectSeries.postValue(defect.getSeries());
                mDefectTitle.postValue(defect.getTitle());
                mDefectSuplier.postValue(defect.getSuplier());
                mDefectCountry.postValue(defect.getCountry());
                mDefectDeliveryId = defect.getDeliveryId();
                mDefectComment.postValue(defect.getComment());
                mDefectAmount.postValue(defect.getQuantity());
                mDefectWriteoff.postValue(defect.getWriteoff());
                mDefectDelivery.postValue(defect.getDeliveryNumber());
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
        mDefectTitle.postValue(good.getGood());
        mDefectSuplier.postValue(good.getSuplier());
        mDefectCountry.postValue(good.getCountry());
        mDefectDelivery.postValue(good.getDeliveryNumber());
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

        if(mDefectSeries.getValue() == null || mDefectSeries.getValue() == "") return;

        DefectEntity defect = new DefectEntity();
        defect.setId(mDefectId);
        defect.setQuantity(mDefectAmount.getValue());
        defect.setWriteoff(mDefectWriteoff.getValue());
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

    public MutableLiveData<String> getDefectTitle() {
        return mDefectTitle;
    }

    public MutableLiveData<String> getDefectSuplier() {
        return mDefectSuplier;
    }

    public MutableLiveData<String> getDefectCountry() {
        return mDefectCountry;
    }

    public MutableLiveData<String> getDefectDelivery() {
        return mDefectDelivery;
    }

    public void setWriteoff(int value) {
        if(mDefectWriteoff.getValue() != value)
            mDefectWriteoff.postValue(value);
    }

    public void incWriteoff() {
        int value = mDefectWriteoff.getValue();
        value++;
        mDefectWriteoff.postValue(value);
    }

    public void decWriteoff() {
        int value = mDefectWriteoff.getValue();
        value = value == 0 ? 0 : value-1;
        mDefectWriteoff.postValue(value);
    }

    public MutableLiveData<Integer> getDefectWriteoff() {
        return mDefectWriteoff;
    }

    public void setPhotoPath(String photoPath) {
        photoPaths.add(photoPath);
    }

    public List<Good> findGoodsByBarcode(String barcode){
        final List<Good> selectedGoods = new ArrayList<>();

        if(mGoods == null) return selectedGoods;
        for (Good good:mGoods.getValue()) {
            if (good.getSeries().equals(barcode)){
                selectedGoods.add(good);
            }
        }

        return selectedGoods;
    }
}
