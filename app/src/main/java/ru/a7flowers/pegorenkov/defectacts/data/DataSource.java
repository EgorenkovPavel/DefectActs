package ru.a7flowers.pegorenkov.defectacts.data;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public interface DataSource {

    interface LoadDeliveriesCallback{
        void onDeliveriesLoaded(List<Delivery> deliveries);
        void onDeliveryLoadFailed();
    }

    interface LoadGoodsCallback{
        void onGoodsLoaded(List<Good> goods);
        void onGoodsLoadFailed();
    }

    interface LoadDefectsCallback{
        void onDefectsLoaded(List<Defect> defects);
        void onDefectsLoadFailed();
    }

    interface LoadDefectCallback{
        void onDefectLoaded(Defect defect);
        void onDefectLoadFailed();
    }

    interface LoadDefectReasonsCallback{
        void onDefectReasonsLoaded(List<DefectReason> reasons);
        void onDefectReasonsLoadFailed();
    }

    interface LoadReasonsCallback{
        void onReasonsLoaded(List<Reason> reasons);
        void onReasonsLoadFailed();
    }

    interface SaveReasonsCallback{
        void onReasonsSaved();
        void onReasonsSavingFailed();
    }
}
