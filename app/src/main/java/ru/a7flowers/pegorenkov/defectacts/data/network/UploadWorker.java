package ru.a7flowers.pegorenkov.defectacts.data.network;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.io.File;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.UsersActivity;
import ru.a7flowers.pegorenkov.defectacts.data.local.AppDatabase;
import ru.a7flowers.pegorenkov.defectacts.data.local.LocalDataSource;

public class UploadWorker extends Worker {

    private static final int ERROR_ID = 4354;
    private static final String CHANNEL_ID = "DEFECT_ACTS_ERROR";

    public static final String USER_ID_KEY = "user_id";
    public static final String DELIVERY_ID_KEY = "delivery_id";
    public static final String DEFECT_ID_KEY = "defect_id";
    public static final String DIFF_ID_KEY = "diff_id";
    public static final String PHOTO_PATH_KEY = "photo_path";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context mContext = getApplicationContext();

        final String userId = getInputData().getString(USER_ID_KEY);
        final String deliveryId = getInputData().getString(DELIVERY_ID_KEY);
        final String defectId = getInputData().getString(DEFECT_ID_KEY);
        final String diffId = getInputData().getString(DIFF_ID_KEY);
        final String path = getInputData().getString(PHOTO_PATH_KEY);

        int photoAmount = 0;
        try {
            if (!defectId.isEmpty())
                photoAmount = NetworkDataSource.getInstance().saveDefectPhoto(userId, deliveryId, diffId, path);
            else if(!diffId.isEmpty())
                photoAmount = NetworkDataSource.getInstance().saveDiffPhoto(userId, deliveryId, diffId, path);
            else
                photoAmount = NetworkDataSource.getInstance().saveDeliveryPhoto(userId, deliveryId, path);
        } catch (Exception e) {
            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext,
                    UsersActivity.class), 0);

            Notification notification = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher) // the status icon
                    .setTicker(mContext.getString(R.string.error_upload_photo_message)) // the status text
                    .setWhen(System.currentTimeMillis()) // the time stamp
                    .setContentTitle(mContext.getString(R.string.error_upload_photo_title)) // the label of the entry
                    .setContentText(mContext.getString(R.string.error_upload_photo_message)) // the content of the entry
                    .setContentIntent(contentIntent) // the intent to send when the entry is clicked
                    .setOngoing(true) // make persistent (disable swipe-away)
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
            notificationManager.notify(ERROR_ID, notification);

            return Result.retry();
        }

        deletePhotoFile(path);

        LocalDataSource localDataSource = LocalDataSource.getInstance(AppDatabase.getInstance(mContext));

        if(!defectId.isEmpty())
            localDataSource.setDefectPhotoCount(deliveryId, defectId, photoAmount);
        else if(!diffId.isEmpty())
            localDataSource.setDiffPhotoCount(deliveryId, diffId, photoAmount);
        else
            localDataSource.setDeliveryPhotoCount(deliveryId, photoAmount);

        return Result.success();
    }

    private void deletePhotoFile(String path){
        File file = new File(path);
        file.delete();
    }
}
