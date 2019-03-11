package ru.a7flowers.pegorenkov.defectacts.data.network;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.io.File;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import ru.a7flowers.pegorenkov.defectacts.R;
import ru.a7flowers.pegorenkov.defectacts.UploadPhotoActivity;
import ru.a7flowers.pegorenkov.defectacts.UsersActivity;
import ru.a7flowers.pegorenkov.defectacts.data.entities.UploadPhotoEntity;
import ru.a7flowers.pegorenkov.defectacts.data.local.AppDatabase;
import ru.a7flowers.pegorenkov.defectacts.data.local.LocalDataSource;

public class UploadWorker extends Worker {


    private static final String CHANNEL_ID = "DEFECT_ACTS_ERROR";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context mContext = getApplicationContext();

        LocalDataSource localDataSource = LocalDataSource.getInstance(AppDatabase.getInstance(mContext));
        NetworkDataSource networkDataSource = NetworkDataSource.getInstance();

        boolean tryAgain = false;
        boolean upLimit = false;

        List<UploadPhotoEntity> entities = localDataSource.getUploadPhotos();
        for (UploadPhotoEntity entity:entities) {

            final String userId = entity.getUserId();
            final String deliveryId = entity.getDeliveryId();
            final String defectId = entity.getDefectId();
            final String diffId = entity.getDiffId();
            final String path = entity.getPhotoPath();

            int photoAmount = 0;
            try {
//                photoAmount /=0;

                if (!defectId.isEmpty())
                    photoAmount = networkDataSource.saveDefectPhoto(userId, deliveryId, diffId, path);
                else if(!diffId.isEmpty())
                    photoAmount = networkDataSource.saveDiffPhoto(userId, deliveryId, diffId, path);
                else
                    photoAmount = networkDataSource.saveDeliveryPhoto(userId, deliveryId, path);

            } catch (Exception e) {
                entity.incTryNumber();

                if(entity.isMaxTry()){
                    upLimit = true;
                }else{
                    tryAgain = true;
                }

                localDataSource.updateUploadPhoto(entity);
                e.printStackTrace();
                continue;
            }

            deletePhotoFile(path);

            if(!defectId.isEmpty())
                localDataSource.setDefectPhotoCount(deliveryId, defectId, photoAmount);
            else if(!diffId.isEmpty())
                localDataSource.setDiffPhotoCount(deliveryId, diffId, photoAmount);
            else
                localDataSource.setDeliveryPhotoCount(deliveryId, photoAmount);

            localDataSource.deleteUploadPhoto(entity);

        }

        if(upLimit){
            NotificationManager notification_manager = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Channel Name";
                String description = "Chanel Description";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.BLUE);
                notification_manager.createNotificationChannel(mChannel);
            }

            Intent i = new Intent(mContext, UploadPhotoActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

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
            notificationManager.notify(UploadPhotoActivity.ERROR_ID, notification);
        }

        if (tryAgain)
            return Result.retry();
        else
            return Result.success();
    }

    private void deletePhotoFile(String path){
        File file = new File(path);
        file.delete();
    }
}
