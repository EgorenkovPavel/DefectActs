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
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

public class DiffViewModel extends AndroidViewModel {

    private Repository mRepository;

    // General
    private LiveData<List<Good>> mGoods;
    private boolean isNewViewModel;

    //DIFF
    private MutableLiveData<Diff> mDiff = new MutableLiveData<>();
    //private MutableLiveData<Good> mDiffGood = new MutableLiveData<>();
    private MutableLiveData<Integer> mPhotoCount = new MutableLiveData<>();
    private List<String> photoPaths = new ArrayList<>();

    public DiffViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;

        init();
    }

    private void saveState(){
        if(isNewViewModel) return;

        Repository.DiffData data = new Repository.DiffData();

        data.setmGoods(mGoods);
        data.setmDiff(mDiff.getValue());
        data.setPhotoPaths(photoPaths);

        mRepository.saveDiffData(data);
    }

    public void restoreState(){
        if(!isNewViewModel) return;

        Repository.DiffData data = mRepository.getSavedDiffData();

        if (data == null) return;

        mGoods = data.getmGoods();
        mDiff.setValue(data.getmDiff());
        photoPaths = data.getPhotoPaths();
        mPhotoCount.setValue(photoPaths.size());

        isNewViewModel = false;
    }

    @Override
    protected void onCleared() {
        saveState();
        super.onCleared();
    }

    private void init(){
        mDiff.setValue(new Diff());
        photoPaths = new ArrayList<>();
        mPhotoCount.setValue(0);

        isNewViewModel = true;
    }

    public void start(String[] deliveryIds){
        loadDelivery(deliveryIds);
        isNewViewModel = false;
    }

    public void start(String[] deliveryIds, String diffId){
        loadDelivery(deliveryIds);

        mRepository.getDiff(diffId, new DataSource.LoadDiffCallback() {
            @Override
            public void onDiffLoaded(final Diff diff) {
                mDiff.postValue(diff);
            }

            @Override
            public void onDiffLoadFailed() {

            }
        });

        isNewViewModel = false;
    }

    private void loadDelivery(String[] deliveryIds){
        mGoods = mRepository.loadGoods(deliveryIds);
    }

    public void setGood(Good good) {
        Diff diff = mDiff.getValue();
        if (diff == null) return;

        diff.setSeries(good.getSeries());
        diff.setDeliveryId(good.getDeliveryId());
        diff.setTitle(good.getGood());
        diff.setSuplier(good.getSuplier());
        diff.setCountry(good.getCountry());
        diff.setDeliveryNumber(good.getDeliveryNumber());

        diff.setListBudgeonAmount(good.getBudgeonAmount());
        diff.setListBulk(good.getBulk());
        diff.setListDiameter(good.getDiameter());
        diff.setListLength(good.getLength());
        diff.setListWeigth(good.getWeigth());

        mDiff.setValue(diff);
    }

    public void setAmount(int value) {
        Diff diff = mDiff.getValue();
        if(diff == null) return;
        diff.setQuantity(value);
        mDiff.setValue(diff);
    }

    public void setComment(String text) {
        Diff diff = mDiff.getValue();
        if(diff == null) return;
        diff.setComment(text);
        mDiff.setValue(diff);
    }

    public void incAmount(){
        Diff diff = mDiff.getValue();
        if(diff == null) return;
        diff.setQuantity(diff.getQuantity() + 1);
        mDiff.setValue(diff);
    }

    public void decAmount(){
        Diff diff = mDiff.getValue();
        if(diff == null) return;
        diff.setQuantity(Math.max(diff.getQuantity() - 1, 0));
        mDiff.setValue(diff);
    }

    public void saveDiff() {

        Diff diff = mDiff.getValue();
        if(diff == null) return;

        mRepository.saveDiff(diff, new ArrayList<>(photoPaths));

        init();
    }

    public LiveData<List<Good>> getGoods() {
        return mGoods;
    }

    public void setPhotoPath(String photoPath) {
        photoPaths.add(photoPath);
        mPhotoCount.postValue(photoPaths.size());
    }

    public MutableLiveData<Integer> getPhotoCount() {
        return mPhotoCount;
    }

    public List<Good> findGoodsByBarcode(String barcode) {
        final List<Good> selectedGoods = new ArrayList<>();

        if(mGoods == null) return selectedGoods;
        for (Good good:mGoods.getValue()) {
            if (good.getSeries().equals(barcode)){
                selectedGoods.add(good);
            }
        }

        return selectedGoods;
    }

    public void setDiffDiameter(float diameter) {
        Diff diff = mDiff.getValue();
        if(diff == null) return;
        diff.setDiameter(diameter);
        mDiff.setValue(diff);
    }

    public void setDiffLength(int length) {
        Diff diff = mDiff.getValue();
        if(diff == null) return;
        diff.setLength(length);
        mDiff.setValue(diff);
    }

    public void setDiffWeigth(int weigth) {
        Diff diff = mDiff.getValue();
        if(diff == null) return;
        diff.setWeigth(weigth);
        mDiff.setValue(diff);
    }

    public void setDiffBudgeonAmount(int budgeonAmount) {
        Diff diff = mDiff.getValue();
        if(diff == null) return;
        diff.setBudgeonAmount(budgeonAmount);
        mDiff.setValue(diff);
    }

    public void setDiffBulk(int bulk) {
        Diff diff = mDiff.getValue();
        if(diff == null) return;
        diff.setBulk(bulk);
        mDiff.setValue(diff);
    }

    public MutableLiveData<Diff> getDiff() {
        return mDiff;
    }

}
