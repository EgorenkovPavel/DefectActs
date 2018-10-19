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
import ru.a7flowers.pegorenkov.defectacts.data.entities.GoodEntity;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

public class DiffViewModel extends AndroidViewModel {

    private Repository mRepository;

    // General
    private String[] mDeliveryIds;
    private LiveData<List<Good>> mGoods;


    private String mDiffId;
    private MutableLiveData<Good> mDiffGood = new MutableLiveData<>();
    private MutableLiveData<Integer> mDiffAmount = new MutableLiveData<>();
    private MutableLiveData<String> mDiffComment = new MutableLiveData<>();

    private MutableLiveData<Integer> mDiffDiameter = new MutableLiveData<>();
    private MutableLiveData<Integer> mDiffLength = new MutableLiveData<>();
    private MutableLiveData<Integer> mDiffWeigth = new MutableLiveData<>();
    private MutableLiveData<Integer> mDiffBudgeonAmount = new MutableLiveData<>();
    private MutableLiveData<Integer> mDiffBulk = new MutableLiveData<>();

    private List<String> photoPaths = new ArrayList<>();

    public DiffViewModel(@NonNull Application application, Repository repository) {
        super(application);
        mRepository = repository;

        init();
    }

    private void init(){
        mDiffId = "";
        mDiffGood.postValue(null);
        mDiffComment.postValue("");
        mDiffAmount.postValue(0);

        mDiffDiameter.postValue(0);
        mDiffLength.postValue(0);
        mDiffWeigth.postValue(0);
        mDiffBudgeonAmount.postValue(0);
        mDiffBulk.postValue(0);

        photoPaths = new ArrayList<>();
    }

    public void start(String[] deliveryIds){
        loadDelivery(deliveryIds);
    }

    public void start(String[] deliveryIds, String diffId){
        loadDelivery(deliveryIds);

        mDiffId = diffId;

        mRepository.getDiff(diffId, new DataSource.LoadDiffCallback() {
            @Override
            public void onDiffLoaded(final Diff diff) {

                mDiffComment.postValue(diff.getComment());
                mDiffAmount.postValue(diff.getQuantity());

                mRepository.getGood(diff.getDeliveryId(), diff.getSeries(), new DataSource.LoadGoodCallback() {
                    @Override
                    public void onGoodLoaded(Good good) {
                        mDiffGood.setValue(good);
                    }

                    @Override
                    public void onGoodLoadFailed() {
                        GoodEntity good = new GoodEntity();
                        good.setSeries(diff.getSeries());
                        good.setGood(diff.getTitle());
                        good.setSuplier(diff.getSuplier());
                        good.setCountry(diff.getCountry());
                        good.setDeliveryId(diff.getDeliveryId());
                        good.setDeliveryNumber(mRepository.getDeliverNumber(diff.getDeliveryId()));
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
        if(mDiffAmount.getValue() != value)
            mDiffAmount.postValue(value);
    }

    public void setComment(String text) {
        if(!mDiffComment.getValue().equals(text))
            mDiffComment.postValue(text);
    }

    public void incAmount(){
        int value = mDiffAmount.getValue();
        value++;
        mDiffAmount.postValue(value);
    }

    public void decAmount(){
        int value = mDiffAmount.getValue();
        value = value == 0 ? 0 : value-1;
        mDiffAmount.postValue(value);
    }

    public void saveDiff() {

        Good good = mDiffGood.getValue();
        if(mDiffGood.getValue() == null) return;

        Diff diff = new Diff();
        diff.setId(mDiffId);
        diff.setQuantity(mDiffAmount.getValue());
        diff.setComment(mDiffComment.getValue());
        diff.setDeliveryId(good.getDeliveryId());
        diff.setSeries(good.getSeries());

        diff.setDiameter(mDiffDiameter.getValue());
        diff.setLength(mDiffLength.getValue());
        diff.setWeigth(mDiffWeigth.getValue());
        diff.setBudgeonAmount(mDiffBudgeonAmount.getValue());
        diff.setBulk(mDiffBulk.getValue());

        mRepository.saveDiff(diff, new ArrayList<>(photoPaths));

        init();
    }

    public MutableLiveData<Good> getDiffGood() {
        return mDiffGood;
    }

    public LiveData<List<Good>> getGoods() {
        return mGoods;
    }

    public LiveData<Integer> getDiffAmount() {
        return mDiffAmount;
    }

    public LiveData<String> getDiffComment() {
        return mDiffComment;
    }

    public void setPhotoPath(String photoPath) {
        photoPaths.add(photoPath);
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

    public MutableLiveData<Integer> getDiffDiameter() {
        return mDiffDiameter;
    }

    public MutableLiveData<Integer> getDiffLength() {
        return mDiffLength;
    }

    public MutableLiveData<Integer> getDiffWeigth() {
        return mDiffWeigth;
    }

    public MutableLiveData<Integer> getDiffBudgeonAmount() {
        return mDiffBudgeonAmount;
    }

    public MutableLiveData<Integer> getDiffBulk() {
        return mDiffBulk;
    }
}
