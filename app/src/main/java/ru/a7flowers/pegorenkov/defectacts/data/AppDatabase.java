package ru.a7flowers.pegorenkov.defectacts.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import ru.a7flowers.pegorenkov.defectacts.data.dao.DefectDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.DefectReasonDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.DeliveryDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.GoodDao;
import ru.a7flowers.pegorenkov.defectacts.data.dao.ReasonDao;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.DefectReason;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

@Database(entities = {Delivery.class, Good.class, Defect.class, Reason.class, DefectReason.class}, version = 1, exportSchema = false)
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
                sInstance.clearAllTables();
            }
        }
        return sInstance;
    }

    public abstract DeliveryDao deliveryDao();

    public abstract GoodDao goodDao();

    public abstract DefectDao defectDao();

    public abstract ReasonDao reasonDao();

    public abstract DefectReasonDao defectReasonDao();
}
