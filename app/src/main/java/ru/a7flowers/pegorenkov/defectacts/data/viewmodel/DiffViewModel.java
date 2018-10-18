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
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;

public class DiffViewModel extends AndroidViewModel {

    private Repository mRepository;

    // General
    private String[] mDeliveryIds;
    private LiveData<List<Good>> mGoods;


    private String mDiffId;
    private String mDiffDeliveryId;
    private MutableLiveData<String> mDiffTitle = new MutableLiveData<>();
    private MutableLiveData<String> mDiffSuplier = new MutableLiveData<>();
    private MutableLiveData<String> mDiffCountry = new MutableLiveData<>();
    private MutableLiveData<String> mDiffDelivery = new MutableLiveData<>();
    private MutableLiveData<Integer> mDiffAmount = new MutableLiveData<>();
    private MutableLiveData<String> mDiffComment = new MutableLiveData<>();
    private MutableLiveData<String> mDiffSeries = new MutableLiveData<>();

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
        mDiffTitle.postValue("");
        mDiffSuplier.postValue("");
        mDiffCountry.postValue("");
        mDiffDelivery.postValue("");
        mDiffComment.postValue("");
        mDiffAmount.postValue(0);
        mDiffSeries.postValue("");

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
            public void onDiffLoaded(Diff diff) {
                mDiffSeries.postValue(diff.getSeries());
                mDiffTitle.postValue(diff.getTitle());
                mDiffSuplier.postValue(diff.getSuplier());
                mDiffCountry.postValue(diff.getCountry());
                mDiffDeliveryId = diff.getDeliveryId();
                mDiffComment.postValue(diff.getComment());
                mDiffAmount.postValue(diff.getQuantity());
                mDiffDelivery.postValue(mRepository.getDeliverNumber(diff.getDeliveryId()));
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
        mDiffSeries.postValue(good.getSeries());
        mDiffDeliveryId = good.getDeliveryId();
        mDiffTitle.postValue(good.getGood());
        mDiffSuplier.postValue(good.getSuplier());
        mDiffCountry.postValue(good.getCountry());
        mDiffDelivery.postValue(mRepository.getDeliverNumber(good.getDeliveryId()));
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
        if(mDiffSeries.getValue() == null || mDiffSeries.getValue().isEmpty()) return;

        Diff diff = new Diff();
        diff.setId(mDiffId);
        diff.setQuantity(mDiffAmount.getValue());
        diff.setSeries(mDiffSeries.getValue());
        diff.setComment(mDiffComment.getValue());
        diff.setDeliveryId(mDiffDeliveryId);
        diff.setDiameter(mDiffDiameter.getValue());
        diff.setLength(mDiffLength.getValue());
        diff.setWeigth(mDiffWeigth.getValue());
        diff.setBudgeonAmount(mDiffBudgeonAmount.getValue());
        diff.setBulk(mDiffBulk.getValue());

        mRepository.saveDiff(diff, new ArrayList<>(photoPaths));

        init();
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

    public LiveData<String> getDiffSeries() {
        return mDiffSeries;
    }

    public LiveData<String> getDiffTitle() {
        return mDiffTitle;
    }

    public LiveData<String> getDiffSuplier() {
        return mDiffSuplier;
    }

    public LiveData<String> getDiffCountry() {
        return mDiffCountry;
    }

    public LiveData<String> getDiffDelivery() {
        return mDiffDelivery;
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

    public MutableLiveData<Integer> getmDiffDiameter() {
        return mDiffDiameter;
    }

    public MutableLiveData<Integer> getmDiffLength() {
        return mDiffLength;
    }

    public MutableLiveData<Integer> getmDiffWeigth() {
        return mDiffWeigth;
    }

    public MutableLiveData<Integer> getmDiffBudgeonAmount() {
        return mDiffBudgeonAmount;
    }

    public MutableLiveData<Integer> getmDiffBulk() {
        return mDiffBulk;
    }
}
