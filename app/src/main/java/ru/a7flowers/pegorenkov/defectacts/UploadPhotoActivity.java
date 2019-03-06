package ru.a7flowers.pegorenkov.defectacts;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.UploadPhotoEntity;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.UploadPhotoViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class UploadPhotoActivity extends AppCompatActivity {

    private UploadPhotoViewModel model;

    private TextView tvDeliveryPhoto;
    private TextView tvDefectPhoto;
    private TextView tvDiffPhoto;
    private Button btnRetry;
    private Button btnClear;
    private Button btnDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        findViews();
        
        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(UploadPhotoViewModel.class);

        model.getFailedPhotos().observe(this, (@Nullable List<UploadPhotoEntity> entities)->{
            int deliveryPhoto = 0;
            int defectPhoto = 0;
            int diffPhoto = 0;

            for (UploadPhotoEntity entity:entities){
                if (entity.isDeliveryPhoto()) deliveryPhoto++;
                else if (entity.isDefectPhoto()) defectPhoto++;
                else if (entity.isDiffPhoto()) diffPhoto++;
            }

            tvDeliveryPhoto.setText(String.valueOf(deliveryPhoto));
            tvDefectPhoto.setText(String.valueOf(defectPhoto));
            tvDiffPhoto.setText(String.valueOf(diffPhoto));
        });
    }

    private void findViews() {
        tvDeliveryPhoto = findViewById(R.id.tvDeliveryFailedPhoto);
        tvDefectPhoto = findViewById(R.id.tvDefectFailedPhoto);
        tvDiffPhoto = findViewById(R.id.tvDiffFailedPhoto);

        btnRetry = findViewById(R.id.btnRetry);
        btnClear = findViewById(R.id.btnClear);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);

        btnRetry.setOnClickListener(view -> {model.retryFailedUploadPhoto(); finish();});
        btnClear.setOnClickListener(view -> {model.cancelFailedUploadPhoto(); finish();});
        btnDeleteAll.setOnClickListener(view -> {model.deleteAllUploadPhoto(); finish();});
    }
}
