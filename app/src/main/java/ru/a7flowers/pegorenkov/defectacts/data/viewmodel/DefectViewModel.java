package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.objects.Defect;
import ru.a7flowers.pegorenkov.defectacts.objects.Delivery;
import ru.a7flowers.pegorenkov.defectacts.objects.Good;
import ru.a7flowers.pegorenkov.defectacts.objects.Reason;

public class DefectViewModel extends AndroidViewModel {

    private Repository mRepository;
    // General
    private Delivery mDelivery;
    private MutableLiveData<List<Good>> mGoods = new MutableLiveData<>();
    private MutableLiveData<List<Reason>> mReasons = new MutableLiveData<>();

    // For defect
    private MutableLiveData<Defect> mDefect = new MutableLiveData<>();

    public DefectViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        mReasons.postValue(mRepository.getReasons());

    }

    public void start(String deliveryId){
        loadDelivery(deliveryId);
        mDefect.postValue(new Defect());
    }

    public void start(String deliveryId, String defectKey){
        loadDelivery(deliveryId);
        for (Defect defect:mDelivery.getDefectAct().getDefects()) {
            if(defect.getKey().equals(defectKey)){
                mDefect.setValue(defect);
            }
        }
    }

    public MutableLiveData<Defect> getDefect() {
        return mDefect;
    }

    private void loadDelivery(String deliveryId){
        mDelivery = mRepository.getDelivery(deliveryId);
        mGoods.postValue(mDelivery.getGoods());
    }

    public MutableLiveData<List<Good>> getGoods() {
        return mGoods;
    }

    public void incAmount(){
        Defect defect = mDefect.getValue();
        int value = defect.getQuantity();
        value++;
        defect.setQuantity(value);
        mDefect.postValue(defect);
    }

    public void decAmount(){
        Defect defect = mDefect.getValue();
        int value = defect.getQuantity();
        value = value == 0 ? 0 : --value;
        defect.setQuantity(value);
        mDefect.postValue(defect);
    }

    public void setGood(Good good){
        mDefect.getValue().setGood(good);
    }

    public void setAmount(int value){
        if(mDefect.getValue() == null){
            return;
        }

        if(mDefect.getValue().getQuantity() != value)
            mDefect.getValue().setQuantity(value);
    }

    public void setComment(String text){
        if(mDefect.getValue() == null){
            return;
        }

        if(!mDefect.getValue().getComment().equals(text))
            mDefect.getValue().setComment(text);
    }

    public MutableLiveData<List<Reason>> getReasons() {
        return mReasons;
    }

    public void setDefectReasons(List<Reason> defectReasons) {
        mDefect.getValue().setReasons(defectReasons);
    }

    public void selectReason(Reason reason){
        List<Reason> defectReasons = mDefect.getValue().getReasons();
        if(defectReasons.contains(reason)){
            defectReasons.remove(reason);
        }else{
            defectReasons.add(reason);
        }
        mDefect.getValue().setReasons(defectReasons);
    }

    public void saveDefect(){
        mRepository.saveDefect(mDelivery, mDefect.getValue());
        mDefect.postValue(new Defect());
    }
}
