package ru.a7flowers.pegorenkov.defectacts.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import ru.a7flowers.pegorenkov.defectacts.data.dao.DefectDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.DefectReasonDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.DeliveryDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.DifferenceDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.GoodDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.ReasonDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.UploadPhotoDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.UserDao;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReasonEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DifferenceEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.GoodEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.UploadPhotoEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBudgeonAmountEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueBulkEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueDiameterEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueLengthEntity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.ValueWeigthEntity;

@Database(entities = {
        User.class,
        Delivery.class,
        GoodEntity.class,
        DefectEntity.class,
        DifferenceEntity.class,
        Reason.class,
        DefectReasonEntity.class,
        ValueDiameterEntity.class,
        ValueBulkEntity.class,
        ValueLengthEntity.class,
        ValueBudgeonAmountEntity.class,
        ValueWeigthEntity.class,
        UploadPhotoEntity.class},

        version = 1, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class AppDatabase extends RoomDatabase{

    private static final String DATABASE_NAME = "defect";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {

        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract UserDao userDao();

    public abstract DeliveryDao deliveryDao();

    public abstract GoodDao goodDao();

    public abstract DefectDao defectDao();

    public abstract ReasonDao reasonDao();

    public abstract DefectReasonDao defectReasonDao();

    public abstract DifferenceDao differenceDao();

    public abstract UploadPhotoDao uploadPhotoDao();

}
