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
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReasonEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

public class DefectViewModel extends AndroidViewModel{

    private Repository mRepository;
    // General
    private LiveData<List<Good>> mGoods;
    private boolean isNewViewModel;

    // For defect
    private MutableLiveData<Defect> mDefect = new MutableLiveData<>();

    private MutableLiveData<Integer> mPhotoCount = new MutableLiveData<>();
    private List<String> photoPaths = new ArrayList<>();
    private String currentPhotoPath;

    public DefectViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;

        init();
    }

    private void saveState(){
       if(isNewViewModel) return;

       Repository.DefectData data = new Repository.DefectData();

       data.setmGoods(mGoods);
       data.setmDefect(mDefect.getValue());
       data.setPhotoPaths(photoPaths);
       data.setCurrentPhotoPath(currentPhotoPath);

       mRepository.saveDefectData(data);
    }

    public void restoreState(){
        if(!isNewViewModel) return;

        Repository.DefectData data = mRepository.getSavedDefectData();

        if (data == null) return;

        mGoods = data.getmGoods();
        mDefect.setValue(data.getmDefect());
        photoPaths = data.getPhotoPaths();
        mPhotoCount.setValue(photoPaths.size());
        currentPhotoPath = data.getCurrentPhotoPath();

        isNewViewModel = false;
    }

    @Override
    protected void onCleared() {
        saveState();
        super.onCleared();
    }

    private void init(){
        mDefect.setValue(new Defect());

        mPhotoCount.postValue(0);
        photoPaths = new ArrayList<>();

        isNewViewModel = true;
    }

    public void start(String[] deliveryIds){
        loadDelivery(deliveryIds);
        isNewViewModel = false;
    }

    public void start(String[] deliveryIds, final String defectId){
        loadDelivery(deliveryIds);

        mRepository.getDefect(defectId, new DataSource.LoadDefectCallback() {
            @Override
            public void onDefectLoaded(Defect defect) {
                mPhotoCount.postValue(0);

                mDefect.postValue(defect);
            }

            @Override
            public void onDefectLoadFailed() {

            }
        });

        isNewViewModel = false;
    }

    private void loadDelivery(String[] deliveryIds){
        mGoods = mRepository.loadGoods(deliveryIds);
    }

    public LiveData<List<Good>> getGoods() {
        return mGoods;
    }

    public void incAmount(){
        Defect defect = mDefect.getValue();
        if(defect == null) return;

        defect.setQuantity(defect.getQuantity()+1);
        mDefect.setValue(defect);

    }

    public void decAmount(){
        Defect defect = mDefect.getValue();
        if(defect == null) return;

        defect.setQuantity(Math.max(defect.getQuantity()-1, 0));
        mDefect.setValue(defect);
    }

    public void setGood(Good good){
        Defect defect = mDefect.getValue();

        defect.setSeries(good.getSeries());
        defect.setDeliveryId(good.getDeliveryId());
        defect.setTitle(good.getGood());
        defect.setSuplier(good.getSuplier());
        defect.setCountry(good.getCountry());
        defect.setDeliveryNumber(good.getDeliveryNumber());

        mDefect.setValue(defect);
    }

    public void setAmount(int value){
        Defect defect = mDefect.getValue();
        if(defect == null) return;
        defect.setQuantity(value);
        mDefect.setValue(defect);
    }

    public void setComment(String value){
        Defect defect = mDefect.getValue();
        if(defect == null) return;
        defect.setComment(value);
        mDefect.setValue(defect);
    }

    public void setDefectReasons(List<Reason> defectReasons) {
        Defect defect = mDefect.getValue();
        if(defect == null) return;

        List<DefectReasonEntity> reasons = new ArrayList<>();
        for (Reason res:defectReasons) {
            reasons.add(new DefectReasonEntity(defect.getId(), res.getId(), res.getTitle()));
        }

        defect.setReasons(reasons);
        mDefect.setValue(defect);
    }

    public MutableLiveData<Defect> getDefect() {
        return mDefect;
    }

    public void saveDefect(){
        Defect defect = mDefect.getValue();
        if(defect == null) return;

        mRepository.saveDefect(defect,
                new ArrayList<>(photoPaths));

        init();
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    public String[] getDefectReasonsList(){
        Defect defect = mDefect.getValue();
        if (defect == null) return new String[0];

        List<DefectReasonEntity> list = defect.getReasons();
        String[] reasons = new String[list.size()];
        for (int i=0;i<list.size();i++){
            reasons[i] = list.get(i).getReasonId();
        }

        return reasons;
    }

    public void savePhoto() {
        photoPaths.add(currentPhotoPath);
    }

    public void setWriteoff(int value) {
        Defect defect = mDefect.getValue();
        if(defect == null) return;
        defect.setWriteoff(value);
        mDefect.setValue(defect);
    }

    public void incWriteoff() {
        Defect defect = mDefect.getValue();
        if(defect == null) return;
        defect.setWriteoff(defect.getWriteoff() + 1);
        mDefect.setValue(defect);
    }

    public void decWriteoff() {

        Defect defect = mDefect.getValue();
        if(defect == null) return;
        defect.setWriteoff(Math.max(defect.getWriteoff() - 1, 0));
        mDefect.setValue(defect);
    }

    public MutableLiveData<Integer> getPhotoCount() {
        return mPhotoCount;
    }

    public void setPhotoPath(String photoPath) {
        photoPaths.add(photoPath);
        mPhotoCount.postValue(photoPaths.size());
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
