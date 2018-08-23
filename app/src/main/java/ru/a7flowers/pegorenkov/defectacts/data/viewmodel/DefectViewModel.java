package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.objects.Good;
import ru.a7flowers.pegorenkov.defectacts.objects.Reason;

public class DefectViewModel extends AndroidViewModel {

    private Repository mRepository;
    // General
    private MutableLiveData<List<Good>> mGoods = new MutableLiveData<>();
    private List<Reason> mReasons;

    // For defect
    private MutableLiveData<Integer> mDefectAmount = new MutableLiveData<>();
    private MutableLiveData<List<Reason>> mDefectReasons = new MutableLiveData<>();


    public DefectViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        mGoods.postValue(mRepository.getGoods());
        mReasons = mRepository.getReasons();

        mDefectAmount.postValue(0);
    }

    public MutableLiveData<List<Good>> getGoods() {
        return mGoods;
    }

    public MutableLiveData<Integer> getAmount() {
        return mDefectAmount;
    }

    public void incAmount(){
        int value = mDefectAmount.getValue();
        value++;
        mDefectAmount.postValue(value);
    }

    public void decAmount(){
        int value = mDefectAmount.getValue();
        value--;
        mDefectAmount.postValue(value);
    }

}
