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

    private Repository mRepository;

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
        List<Reason> defectReasons = mDefectReasons.getValue();
        if(defectReasons.contains(reason)){
            defectReasons.remove(reason);
        }else{
            defectReasons.add(reason);
        }
        mDefectReasons.postValue(defectReasons);
    }

    public LiveData<List<Reason>> getReasons() {
        return mReasons;
    }

    public MutableLiveData<List<Reason>> getDefectReasons() {
        return mDefectReasons;
    }

}
