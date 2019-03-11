package ru.a7flowers.pegorenkov.defectacts;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.UploadPhotoEntity;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.UploadPhotoViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class UploadPhotoActivity extends AppCompatActivity {

    public static final int ERROR_ID = 4354;

    private UploadPhotoViewModel model;

    private TextView tvAllPhoto;
    private TextView tvFailedPhoto;
    private TextView tvDeliveryPhoto;
    private TextView tvDefectPhoto;
    private TextView tvDiffPhoto;
    private ImageButton btnUploadAll;
    private ImageButton btnClearAll;
    private ImageButton btnUploadFailed;
    private ImageButton btnClearFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViews();
        
        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(UploadPhotoViewModel.class);

        model.getPhotos().observe(this, (@Nullable List<UploadPhotoEntity> entities)->{
            int allPhoto = 0;
            if(entities != null) allPhoto = entities.size();
            tvAllPhoto.setText(String.valueOf(allPhoto));
        });

        model.getFailedPhotos().observe(this, (@Nullable List<UploadPhotoEntity> entities)->{
            int failedPhoto = 0;
            int deliveryPhoto = 0;
            int defectPhoto = 0;
            int diffPhoto = 0;

            if(entities != null) {
                for (UploadPhotoEntity entity : entities) {
                    failedPhoto++;

                    if (entity.isDeliveryPhoto()) deliveryPhoto++;
                    else if (entity.isDefectPhoto()) defectPhoto++;
                    else if (entity.isDiffPhoto()) diffPhoto++;
                }
            }

            tvFailedPhoto.setText(String.valueOf(failedPhoto));
            tvDeliveryPhoto.setText(String.valueOf(deliveryPhoto));
            tvDefectPhoto.setText(String.valueOf(defectPhoto));
            tvDiffPhoto.setText(String.valueOf(diffPhoto));
        });

        model.isClose().observe(this, (close)->{
            if(close == true) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(UploadPhotoActivity.this);
                notificationManager.cancel(ERROR_ID);

                finish();
            };
        });
    }

    private void findViews() {
        tvAllPhoto = findViewById(R.id.tvAllPhoto);
        tvFailedPhoto = findViewById(R.id.tvFailedPhoto);
        tvDeliveryPhoto = findViewById(R.id.tvDeliveryFailedPhoto);
        tvDefectPhoto = findViewById(R.id.tvDefectFailedPhoto);
        tvDiffPhoto = findViewById(R.id.tvDiffFailedPhoto);

        btnUploadAll = findViewById(R.id.btnUploadAll);
        btnClearAll = findViewById(R.id.btnClearAll);
        btnUploadFailed = findViewById(R.id.btnUploadFailed);
        btnClearFailed = findViewById(R.id.btnClearFailed);

        btnUploadAll.setOnClickListener(view -> model.uploadAllPhotos());
        btnClearAll.setOnClickListener(view -> showDeleteDialog(()->model.clearAllPhotos()));
        btnUploadFailed.setOnClickListener(view -> model.uploadFailedPhotos());
        btnClearFailed.setOnClickListener(view -> showDeleteDialog(()->model.clearFailedPhotos()));
    }

    private void showDeleteDialog(OnDialogClick onClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_photos_dialog_title)
                .setMessage(R.string.delete_photo_dialog_message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    onClick.click();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    dialog.cancel();
                });
        builder.show();
    }

    @FunctionalInterface
    private interface OnDialogClick{
        void click();
    }
}
