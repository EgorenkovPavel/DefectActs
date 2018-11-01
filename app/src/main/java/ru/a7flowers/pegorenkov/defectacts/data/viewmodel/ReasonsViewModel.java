package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class ReasonsViewModel extends AndroidViewModel {

    @SuppressWarnings("FieldCanBeLocal")
    private Repository mRepository;

    private LiveData<List<Reason>> mReasons;
    private Set<String> mDefectReasonsIds = new HashSet<>();

    public ReasonsViewModel(@NonNull Application application, Repository repository) {
        super(application);

        mRepository = repository;

        mReasons = repository.getReasons();
    }

    public void setDefectReasons(String[] reasons){
        mDefectReasonsIds = new HashSet<>(Arrays.asList(reasons));
    }

    public LiveData<List<Reason>> getReasons() {
        return mReasons;
    }

    public boolean isReasonSelected(Reason reason) {
        return mDefectReasonsIds.contains(reason.getId());
    }

    public void addSelectedReason(Reason reason) {
        mDefectReasonsIds.add(reason.getId());
    }

    public void removeSelectedReason(Reason reason) {
        mDefectReasonsIds.remove(reason.getId());
    }

    public List<Reason> getDefectReasons() {
        List<Reason> reasons = mReasons.getValue();
        List<Reason> selectedReasons = new ArrayList<>();

        for (Reason reason:reasons) {
            if(isReasonSelected(reason))
                selectedReasons.add(reason);
        }

        return selectedReasons;
    }
}
