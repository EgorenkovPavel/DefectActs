package ru.a7flowers.pegorenkov.defectacts.data.network;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class DefectWithReasons {

    @Embedded
    private Defect mDefect;

    @Embedded(prefix = "good_")
    transient private Good mGood; //ignore in retrofit

    @SerializedName("reasons")
    @Relation(parentColumn = "id", entityColumn = "defectId", entity = DefectReason.class)
    private List<DefectReason> mReasons;

    public DefectWithReasons() {
    }

    @Ignore
    public DefectWithReasons(Defect defect, List<Reason> reasons) {
        mDefect = defect;
        mReasons = new ArrayList<>();
        for (Reason reason:reasons) {
            mReasons.add(new DefectReason(defect.getId(), reason.getId()));
        }
    }

    public List<DefectReason> getReasons() {
        return mReasons;
    }

    public void setReasons(List<DefectReason> reasons) {
        mReasons = reasons;
    }

    public Defect getDefect() {
        return mDefect;
    }

    public void setDefect(Defect defect) {
        mDefect = defect;
    }

    public String getId() {
        return mDefect.getId();
    }

    public String getDeliveryId() {
        return mDefect.getDeliveryId();
    }

    public int getPhotoQuantity() {
        return mDefect.getPhotoQuantity();
    }

    public String getSeries() {
        return mDefect.getSeries();
    }

    public int getQuantity() {
        return mDefect.getQuantity();
    }

    public String getComment() {
        return mDefect.getComment();
    }

    public Good getGood() {
        return mGood;
    }

    public void setGood(Good good) {
        mGood = good;
    }

    public void setId(String id) {
        mDefect.setId(id);
    }

    public String getGoodTitle() {
        return mGood.getGood();
    }

    public String getSuplier() {
        return mGood.getSuplier();
    }

    public String getCountry() {
        return mGood.getCountry();
    }
}
