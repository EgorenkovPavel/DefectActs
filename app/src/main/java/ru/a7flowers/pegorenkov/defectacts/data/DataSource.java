package ru.a7flowers.pegorenkov.defectacts.data;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;

public interface DataSource {

    interface LoadDeliveriesCallback{
        void onDeliveriesLoaded(List<Delivery> deliveries);
        void onDeliveriesLoadFailed();
    }

    interface LoadDeliveryCallback{
        void onDeliveryLoaded(Delivery delivery);
        void onDeliveryLoadFailed();
    }

    interface LoadGoodsCallback{
        void onGoodsLoaded(List<Good> goods);
        void onGoodsLoadFailed();
    }

    interface LoadDefectsCallback{
        void onDefectsLoaded(List<DefectWithReasons> defects);
        void onDefectsLoadFailed();
    }

    interface LoadDefectCallback{
        void onDefectLoaded(DefectWithReasons defect);
        void onDefectLoadFailed();
    }

    interface LoadReasonsCallback{
        void onReasonsLoaded(List<Reason> reasons);
        void onReasonsLoadFailed();
    }

    interface SaveReasonsCallback{
        void onReasonsSaved();
        void onReasonsSavingFailed();
    }

    interface UploadDefectCallback{
        void onDefectUploaded(DefectWithReasons defect);
        void onDefectUploadingFailed();
    }

    interface UploadPhotosCallback{
        void onPhotosUploaded();
        void onPhotosUploadingFailed();
    }

    interface ClearDatabaseCallback{
        void onDatabaseCleared();
        void onDatabaseClearingFailed();
    }

    interface ReloadDataCallback{
        void onDataReloaded();
        void onDataReloadingFailed();
    }
}
