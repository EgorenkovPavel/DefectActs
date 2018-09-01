package ru.a7flowers.pegorenkov.defectacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DefectViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class DefectActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_BARCODE_CAPTURE = 9001;
    public static final String DELIVERY = "delivery_id";
    public static final String DEFECT = "defect_key";

    private static final int SELECT_REASON = 47;
    public static final String SELECTED_REASONS = "selected_reasons";

    private DefectViewModel model;

    private GoodsAdapter adapter;

    private Spinner sGoods;
    private EditText etAmount;
    private TextView tvReasons;
    private EditText etComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(DefectViewModel.class);

        findViews();

        Intent i = getIntent();
        if (i.hasExtra(DELIVERY)){
            String[] deliveryIds = i.getExtras().getStringArray(DELIVERY);
            if(i.hasExtra(DEFECT)){
                String defectId = i.getStringExtra(DEFECT);
                model.start(deliveryIds, defectId);
            }else{
                model.start(deliveryIds);
            }
        }

        adapter = new GoodsAdapter(this);
        sGoods.setAdapter(adapter);

        model.getGoods().observe(this, new Observer<List<Good>>() {
            @Override
            public void onChanged(@Nullable List<Good> goods) {
                adapter.clear();
                if(goods != null) {
                    adapter.addAll(goods);
                    setCurrentSeries(model.getCurrentSeries());
                }
            }
        });
        model.getDefectAmount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer value) {
                String text = String.valueOf(value);
                if(!text.equals(etAmount.getText().toString())) {
                    etAmount.setText(text);
                    etAmount.setSelection(etAmount.getText().length());
                }
            }
        });
        model.getDefectComment().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String comment) {
                String text = String.valueOf(comment);
                if(!text.equals(etComment.getText().toString())) {
                    etComment.setText(comment);
                    etComment.setSelection(etComment.getText().length());
                }
            }
        });
        model.getDefectSeries().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String series) {
                setCurrentSeries(series);
            }
        });
        model.getDefectReasons().observe(this, new Observer<List<Reason>>() {
            @Override
            public void onChanged(@Nullable List<Reason> reasons) {
                if (reasons == null){
                    tvReasons.setText("");
                    return;
                }

                StringBuilder text = new StringBuilder();
                for (Reason reason:reasons) {
                    text.append(reason.getTitle()).append("; ");
                }
                tvReasons.setText(text.toString());
            }
        });
    }

    private void findViews() {
        sGoods = findViewById(R.id.sGoods);
        tvReasons = findViewById(R.id.tvReasons);
        etAmount = findViewById(R.id.etAmount);
        etComment = findViewById(R.id.etComment);
        ImageButton btnInc = findViewById(R.id.btnInc);
        ImageButton btnDec = findViewById(R.id.btnDec);
        ImageButton ibPhoto = findViewById(R.id.ibPhoto);
        ImageButton ibNext = findViewById(R.id.ibNext);
        ImageButton ibBarcode = findViewById(R.id.ibBarcode);

        sGoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Good good = (Good) adapterView.getAdapter().getItem(i);
                model.setGood(good);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvReasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseReason();
            }
        });

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = String.valueOf(etAmount.getText());
                int value = text.isEmpty() ? 0 : Integer.valueOf(text);
                model.setAmount(value);
            }
        });

        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                model.setComment(String.valueOf(etComment.getText()));
            }
        });

        btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.incAmount();
            }
        });

        btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.decAmount();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.saveDefect();
            }
        });

        ibBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DefectActivity.this, BarcodeScannerActivity.class);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
        });

        ibPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pCamera = ActivityCompat.checkSelfPermission(DefectActivity.this, Manifest.permission.CAMERA);
                int pStorage = ActivityCompat.checkSelfPermission(DefectActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (pCamera == PackageManager.PERMISSION_GRANTED
                        && pStorage == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    List<String> per = new ArrayList<>();
                    if(pCamera == PackageManager.PERMISSION_DENIED)
                        per.add(Manifest.permission.CAMERA);
                    if(pStorage == PackageManager.PERMISSION_DENIED)
                        per.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    final String[] permissions = per.toArray(new String[per.size()]);
                    ActivityCompat.requestPermissions(DefectActivity.this, permissions, RC_HANDLE_CAMERA_PERM);
                }
             }
        });
    }

    private void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(DefectActivity.this,
                        "ru.a7flowers.pegorenkov.defectacts.GenericFileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        model.setCurrentPhotoPath(image.getAbsolutePath());

        return image;
    }

    private void chooseReason(){
        Intent i = new Intent(this, ReasonsActivity.class);
        i.putExtra(SELECTED_REASONS, (Serializable) model.getDefectReasonsList());
        startActivityForResult(i, SELECT_REASON);
    }

    private void setCurrentSeries(String series){
        for (int i = 0; i<adapter.getCount(); i++){
            Good good = adapter.getItem(i);
            if(good.getSeries().equals(series)){
                sGoods.setSelection(i);
                return;
            }
        }

        if (!series.isEmpty())
        Toast.makeText(this, "Barcode " + series + " not found", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        boolean granted = grantResults.length != 0;
        for (int i=0; i<permissions.length;i++){
            granted = granted && grantResults[i] == PackageManager.PERMISSION_GRANTED;
        }

        if (granted) {
            takePicture();
            return;
        }

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == SELECT_REASON && resultCode == RESULT_OK){
            if(intent.hasExtra(SELECTED_REASONS)) {
                List<Reason> list = (List<Reason>) intent.getExtras().getSerializable(SELECTED_REASONS);
                model.setDefectReasons(list);
            }
        }else if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (intent != null) {
                    String barcode = intent.getStringExtra(BarcodeScannerActivity.BARCODE);
                    model.setSeries(barcode);
                    Toast.makeText(this, barcode, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.barcode_failure, Toast.LENGTH_LONG).show();
                }
            }
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            model.savePhoto();
        }
        else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    class GoodsAdapter extends ArrayAdapter<Good>{

        public GoodsAdapter(@NonNull Context context) {
            super(context, R.layout.item_good);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return inflateView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return inflateView(position, convertView, parent);
        }

        private View inflateView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_good, parent, false);
            Good good = getItem(position);

            TextView tvSeries = v.findViewById(R.id.tvSeries);
            TextView tvGood = v.findViewById(R.id.tvGood);
            TextView tvSuplier = v.findViewById(R.id.tvSuplier);
            TextView tvCounty = v.findViewById(R.id.tvCountry);
            TextView tvAmount = v.findViewById(R.id.tvQuantity);

            tvSeries.setText(good.getSeries());
            tvGood.setText(good.getGood());
            tvSuplier.setText(good.getSuplier());
            tvCounty.setText(good.getCountry());
            tvAmount.setText(String.valueOf(good.getDeliveryQuantity()));

            return v;
        }
    }
}
