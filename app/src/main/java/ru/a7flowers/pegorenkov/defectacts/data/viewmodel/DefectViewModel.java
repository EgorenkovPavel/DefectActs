package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.DataSource;
import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;

public class DefectViewModel extends AndroidViewModel{

    private Repository mRepository;
    // General
    private String[] mDeliveryIds;
    private LiveData<List<Good>> mGoods;
    private boolean isNewViewModel;

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

    public void saveData(){
        if(isNewViewModel) return;

        ViewModelData data = new ViewModelData();
        data.setmDeliveryIds(mDeliveryIds);
        data.setmGoods(mGoods);
        data.setmDefectId(mDefectId);
        data.setmDefectDeliveryId(mDefectDeliveryId);
        data.setmDefectTitle(getStringValue(mDefectTitle));
        data.setmDefectSuplier(getStringValue(mDefectSuplier));
        data.setmDefectCountry(getStringValue(mDefectCountry));
        data.setmDefectDelivery(getStringValue(mDefectDelivery));
        data.setmDefectAmount(getIntegerValue(mDefectAmount));
        data.setmDefectWriteoff(getIntegerValue(mDefectWriteoff));
        data.setmDefectComment(getStringValue(mDefectComment));
        data.setmDefectSeries(getStringValue(mDefectSeries));
        data.setmDefectReasons(mDefectReasons.getValue());
        data.setPhotoPaths(photoPaths);
        data.setCurrentPhotoPath(currentPhotoPath);

        mRepository.setSavedData(data);
    }

    public void restoreData(){
        if(!isNewViewModel) return;

        ViewModelData data = mRepository.getSavedData();

        if (data == null) return;

        mDeliveryIds = data.getmDeliveryIds();
        mGoods = data.getmGoods();
        mDefectId = data.getmDefectId();
        mDefectDeliveryId = data.getmDefectDeliveryId();
        mDefectTitle.setValue(data.getmDefectTitle());
        mDefectSuplier.setValue(data.getmDefectSuplier());
        mDefectCountry.setValue(data.getmDefectCountry());
        mDefectDelivery.setValue(data.getmDefectDelivery());
        mDefectAmount.setValue(data.getmDefectAmount());
        mDefectWriteoff.setValue(data.getmDefectWriteoff());
        mDefectComment.setValue(data.getmDefectComment());
        mDefectSeries.setValue(data.getmDefectSeries());
        mDefectReasons.setValue(data.getmDefectReasons());
        photoPaths = data.getPhotoPaths();
        currentPhotoPath = data.getCurrentPhotoPath();

        isNewViewModel = false;
    }

    @Override
    protected void onCleared() {
        saveData();
        super.onCleared();
    }

    public class ViewModelData{
        private String[] mDeliveryIds;
        private LiveData<List<Good>> mGoods;

        private String mDefectId;
        private String mDefectDeliveryId;
        private String mDefectTitle;
        private String mDefectSuplier;
        private String mDefectCountry;
        private String mDefectDelivery;
        private Integer mDefectAmount;
        private Integer mDefectWriteoff;
        private String mDefectComment;
        private String mDefectSeries;
        private List<Reason> mDefectReasons;
        private List<String> photoPaths;
        private String currentPhotoPath;

        public String[] getmDeliveryIds() {
            return mDeliveryIds;
        }

        public void setmDeliveryIds(String[] mDeliveryIds) {
            this.mDeliveryIds = mDeliveryIds;
        }

        public LiveData<List<Good>> getmGoods() {
            return mGoods;
        }

        public void setmGoods(LiveData<List<Good>> mGoods) {
            this.mGoods = mGoods;
        }

        public String getmDefectId() {
            return mDefectId;
        }

        public void setmDefectId(String mDefectId) {
            this.mDefectId = mDefectId;
        }

        public String getmDefectDeliveryId() {
            return mDefectDeliveryId;
        }

        public void setmDefectDeliveryId(String mDefectDeliveryId) {
            this.mDefectDeliveryId = mDefectDeliveryId;
        }

        public String getmDefectTitle() {
            return mDefectTitle;
        }

        public void setmDefectTitle(String mDefectTitle) {
            this.mDefectTitle = mDefectTitle;
        }

        public String getmDefectSuplier() {
            return mDefectSuplier;
        }

        public void setmDefectSuplier(String mDefectSuplier) {
            this.mDefectSuplier = mDefectSuplier;
        }

        public String getmDefectCountry() {
            return mDefectCountry;
        }

        public void setmDefectCountry(String mDefectCountry) {
            this.mDefectCountry = mDefectCountry;
        }

        public String getmDefectDelivery() {
            return mDefectDelivery;
        }

        public void setmDefectDelivery(String mDefectDelivery) {
            this.mDefectDelivery = mDefectDelivery;
        }

        public Integer getmDefectAmount() {
            return mDefectAmount;
        }

        public void setmDefectAmount(Integer mDefectAmount) {
            this.mDefectAmount = mDefectAmount;
        }

        public Integer getmDefectWriteoff() {
            return mDefectWriteoff;
        }

        public void setmDefectWriteoff(Integer mDefectWriteoff) {
            this.mDefectWriteoff = mDefectWriteoff;
        }

        public String getmDefectComment() {
            return mDefectComment;
        }

        public void setmDefectComment(String mDefectComment) {
            this.mDefectComment = mDefectComment;
        }

        public String getmDefectSeries() {
            return mDefectSeries;
        }

        public void setmDefectSeries(String mDefectSeries) {
            this.mDefectSeries = mDefectSeries;
        }

        public List<Reason> getmDefectReasons() {
            return mDefectReasons;
        }

        public void setmDefectReasons(List<Reason> mDefectReasons) {
            this.mDefectReasons = mDefectReasons;
        }

        public List<String> getPhotoPaths() {
            return photoPaths;
        }

        public void setPhotoPaths(List<String> photoPaths) {
            this.photoPaths = photoPaths;
        }

        public String getCurrentPhotoPath() {
            return currentPhotoPath;
        }

        public void setCurrentPhotoPath(String currentPhotoPath) {
            this.currentPhotoPath = currentPhotoPath;
        }
    }

    private void init(){
        mDefectId = "";
        mDefectTitle.postValue("");
        mDefectSuplier.setValue("");
        mDefectCountry.setValue("");
        mDefectDelivery.setValue("");
        mDefectComment.setValue("");
        mDefectAmount.setValue(0);
        mDefectWriteoff.setValue(0);
        mDefectSeries.setValue("");
        mDefectReasons.setValue(new ArrayList<Reason>());
        photoPaths = new ArrayList<>();

        isNewViewModel = true;
    }

    public void start(String[] deliveryIds){
        loadDelivery(deliveryIds);
        isNewViewModel = false;
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
                mDefectDelivery.postValue(mRepository.getDeliverNumber(defect.getDeliveryId()));
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
        isNewViewModel = false;
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
        Integer value = getIntegerValue(mDefectAmount);
        value++;
        mDefectAmount.setValue(value);
    }

    public void decAmount(){
        Integer value = getIntegerValue(mDefectAmount);
        value = value == 0 ? 0 : value-1;
        mDefectAmount.setValue(value);
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
        mDefectSeries.setValue(good.getSeries());
        mDefectDeliveryId = good.getDeliveryId();
        mDefectTitle.setValue(good.getGood());
        mDefectSuplier.setValue(good.getSuplier());
        mDefectCountry.setValue(good.getCountry());
        mDefectDelivery.setValue(mRepository.getDeliverNumber(good.getDeliveryId()));
    }

    public void setAmount(int value){
        if(getIntegerValue(mDefectAmount) != value)
            mDefectAmount.setValue(value);
    }

    public void setComment(String text){
        if(!getStringValue(mDefectComment).equals(text))
            mDefectComment.setValue(text);
    }

    public void setDefectReasons(List<Reason> defectReasons) {
        mDefectReasons.postValue(defectReasons);
    }

    public void saveDefect(){

        String series = mDefectSeries.getValue();
        if(series == null || series.isEmpty()) return;

        Defect defect = new Defect();
        defect.setId(mDefectId);
        defect.setQuantity(getIntegerValue(mDefectAmount));
        defect.setWriteoff(getIntegerValue(mDefectWriteoff));
        defect.setSeries(getStringValue(mDefectSeries));
        defect.setComment(getStringValue(mDefectComment));
        defect.setDeliveryId(mDefectDeliveryId);

        List<Reason> reasons = mDefectReasons.getValue();
        if(reasons == null) reasons = new ArrayList<>();

        DefectWithReasons defectWithReasons = new DefectWithReasons(defect, reasons);

        mRepository.saveDefect(defectWithReasons,
                new ArrayList<>(photoPaths));

        init();
    }

    private int getIntegerValue(MutableLiveData<Integer> data){
        Integer value = data.getValue();
        if (value == null) value = 0;
        return value;
    }

    private String getStringValue(MutableLiveData<String> data){
        String value = data.getValue();
        if (value == null) value = "";
        return value;
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

    public String getDeliveryNumber(String deliveryId) {
        //TODO add delivery number to defectWithReasons and Good
        return mRepository.getDeliverNumber(deliveryId);
    }

    public void setWriteoff(int value) {
        if(getIntegerValue(mDefectWriteoff) != value)
            mDefectWriteoff.postValue(value);
    }

    public void incWriteoff() {
        int value = getIntegerValue(mDefectWriteoff);
        value++;
        mDefectWriteoff.postValue(value);
    }

    public void decWriteoff() {
        int value = getIntegerValue(mDefectWriteoff);
        value = value == 0 ? 0 : value-1;
        mDefectWriteoff.postValue(value);
    }

    public MutableLiveData<Integer> getDefectWriteoff() {
        return mDefectWriteoff;
    }

}
