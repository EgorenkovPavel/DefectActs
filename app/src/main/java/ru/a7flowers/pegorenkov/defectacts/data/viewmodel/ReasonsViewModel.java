package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class ReasonsViewModel extends AndroidViewModel {

    @SuppressWarnings("FieldCanBeLocal")
    private Repository mRepository;

    //TODO rewrite ALL!!!!!!!!!

    private LiveData<List<Reason>> mReasons;
    private MutableLiveData<List<Reason>> mDefectReasons = new MutableLiveData<>();

    public ReasonsViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        mReasons = repository.getReasons();
        mDefectReasons.postValue(new ArrayList<Reason>());
    }

    public void setDefectReasons(List<Reason> reasons){
        mDefectReasons.postValue(reasons);
    }

    public void selectReason(Reason reason){
        Reason res = getReasonById(reason.getId());
        if (res == null) return;

        Reason newRes = null;
        List<Reason> defectReasons = mDefectReasons.getValue();
        for (Reason defRes:defectReasons){
            if(defRes.getId().equals(res.getId())){
                newRes = defRes;
                break;
            }
        }
        if(newRes != null){
            defectReasons.remove(newRes);
        }else{
            defectReasons.add(res);
        }
//        if(defectReasons.contains(res)){
//            defectReasons.remove(res);
//        }else{
//            defectReasons.add(res);
//        }
        mDefectReasons.postValue(defectReasons);
    }

    private Reason getReasonById(String id){
        List<Reason> reasons = mReasons.getValue();
        for (Reason reason:reasons){
            if(reason.getId().equals(id))
                return reason;
        }
        return null;
    }

    public LiveData<List<Reason>> getReasons() {
        return mReasons;
    }

    public MutableLiveData<List<Reason>> getDefectReasons() {
        return mDefectReasons;
    }

}
