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
    private String[] mDeliveryIds;
    private LiveData<List<Good>> mGoods;

    //DIFF
    private MutableLiveData<Diff> mDiff = new MutableLiveData<>();
    private MutableLiveData<Good> mDiffGood = new MutableLiveData<>();
    private MutableLiveData<Integer> mPhotoCount = new MutableLiveData<>();
    private List<String> photoPaths = new ArrayList<>();

    public MutableLiveData<Diff> getDiff() {
        return mDiff;
    }

    public DiffViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;

        init();
    }

    private void init(){
        mDiff.setValue(new Diff());
        mDiffGood.setValue(null);
        photoPaths = new ArrayList<>();
        mPhotoCount.setValue(0);
    }

    public void start(String[] deliveryIds){
        loadDelivery(deliveryIds);
    }

    public void start(String[] deliveryIds, String diffId){
        loadDelivery(deliveryIds);

        mRepository.getDiff(diffId, new DataSource.LoadDiffCallback() {
            @Override
            public void onDiffLoaded(final Diff diff) {

                //TODO Problem: loading good and diff independently. When to set length value to view?
                //using rxjava or call model when draw includes

                mDiff.postValue(diff);

                mRepository.getGood(diff.getDeliveryId(), diff.getSeries(), new DataSource.LoadGoodCallback() {
                    @Override
                    public void onGoodLoaded(Good good) {
                        mDiffGood.postValue(good);
                    }

                    @Override
                    public void onGoodLoadFailed() {
                        Good good = new Good();
                        good.setSeries(diff.getSeries());
                        good.setGood(diff.getTitle());
                        good.setSuplier(diff.getSuplier());
                        good.setCountry(diff.getCountry());
                        good.setDeliveryId(diff.getDeliveryId());
                        good.setDeliveryNumber(diff.getDeliveryId());
                        mDiffGood.postValue(good);
                    }
                });
            }

            @Override
            public void onDiffLoadFailed() {

            }
        });
    }

    private void loadDelivery(String[] deliveryIds){
        mDeliveryIds = deliveryIds;
        mGoods = mRepository.loadGoods(deliveryIds);
    }

    public void setGood(Good good) {
        mDiffGood.setValue(good);
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

    public MutableLiveData<Good> getDiffGood() {
        return mDiffGood;
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

    public int getDiffLength() {
        Diff diff = mDiff.getValue();
        if(diff == null)
            return 0;
        else
            return diff.getLength();
    }

    public float getDiffDiameter() {
        Diff diff = mDiff.getValue();
        if(diff == null)
            return 0;
        else
            return diff.getDiameter();
    }

    public float getDiffBulk() {
        Diff diff = mDiff.getValue();
        if(diff == null)
            return 0;
        else
            return diff.getBulk();
    }

    public int getDiffBudgeonAmount() {
        Diff diff = mDiff.getValue();
        if(diff == null)
            return 0;
        else
            return diff.getBudgeonAmount();
    }

    public int getDiffWeigth() {
        Diff diff = mDiff.getValue();
        if(diff == null)
            return 0;
        else
            return diff.getWeigth();
    }
}
