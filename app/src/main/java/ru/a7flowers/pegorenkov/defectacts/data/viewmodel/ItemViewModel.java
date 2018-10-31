package ru.a7flowers.pegorenkov.defectacts.data.viewmodel;

import android.arch.lifecycle.ViewModel;

import ru.a7flowers.pegorenkov.defectacts.data.Repository;

public abstract class ItemViewModel extends ViewModel {

    private Repository mRepository;
    private boolean isNewViewModel;

    public ItemViewModel(Repository repository) {
        mRepository = repository;
        isNewViewModel = true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
