package ru.a7flowers.pegorenkov.defectacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.a7flowers.pegorenkov.defectacts.data.network.Good;

import static android.support.v7.app.AlertDialog.*;

public class ItemActivity extends AppCompatActivity{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_BARCODE_CAPTURE = 9001;

    private static final String PHOTO_PATH_KEY = "photo_path_key";
    private static final String PHOTO_PARAMS_KEY = "photo_params_key";

    private String mCurrentPhotoPath;
    private Bundle mPhotoParams;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            if (savedInstanceState.containsKey(PHOTO_PATH_KEY))
                mCurrentPhotoPath = savedInstanceState.getString(PHOTO_PATH_KEY);
            if (savedInstanceState.containsKey(PHOTO_PARAMS_KEY))
                mPhotoParams = savedInstanceState.getBundle(PHOTO_PARAMS_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PHOTO_PATH_KEY, mCurrentPhotoPath);
        outState.putBundle(PHOTO_PARAMS_KEY, mPhotoParams);
         super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (showBackpressedDialog())
            getBackPressedDialog().show();
        else
            super.onBackPressed();
    }

    private Dialog getBackPressedDialog(){
        Builder builder = new Builder(this);
        builder.setMessage(R.string.dialog_back_pressed)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> ItemActivity.super.onBackPressed());
        return builder.create();
    }

    public void startPhoto(Bundle photoParams){
        this.mPhotoParams = photoParams;
        checkPermissions(RC_HANDLE_CAMERA_PERM,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    public void startBarcode(){
        checkPermissions(RC_BARCODE_CAPTURE, new String[]{Manifest.permission.CAMERA});
    }

    private void checkPermissions(int requestCode, String[] permissions){

        Map<String, Integer> accesses = new HashMap<>();
        for (String permission:permissions) {
            accesses.put(permission, ActivityCompat.checkSelfPermission(this, permission));
        }

        boolean granted = true;
        List<String> per = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: accesses.entrySet()) {
            granted = granted && entry.getValue().equals(PackageManager.PERMISSION_GRANTED);
            if (entry.getValue().equals(PackageManager.PERMISSION_DENIED)){
                per.add(entry.getKey());
            }
        }

        if (granted){
            startAction(requestCode);
        }else{
            final String[] pers = per.toArray(new String[per.size()]);
            ActivityCompat.requestPermissions(this, pers, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        boolean granted = grantResults.length != 0;
        for (int i = 0; i < permissions.length; i++) {
            granted = granted && grantResults[i] == PackageManager.PERMISSION_GRANTED;
        }

        if (granted) {
            startAction(requestCode);
            return;
        }

        DialogInterface.OnClickListener listener = (dialog, id) -> finish();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, listener)
                .show();

    }

    private void startAction(int requestCode){
        switch (requestCode){
            case RC_BARCODE_CAPTURE:{
                scanBarcode();
                break;
            }
            case RC_HANDLE_CAMERA_PERM:{
                takePicture();
                break;
            }
        }
    }

    private void scanBarcode(){
        Intent intent = new Intent(this, BarcodeScannerActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
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
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ru.a7flowers.pegorenkov.defectacts.GenericFileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (intent != null) {
                    String barcode = intent.getStringExtra(BarcodeScannerActivity.BARCODE);
                    onBarcodeScanned(barcode);
                } else {
                    onScanFailed();
                }
            }
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            deleteLatest();
            onPhotoTaken(mCurrentPhotoPath, mPhotoParams);
        }
        else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void chooseGood(final List<Good> selectedGoods){

        switch (selectedGoods.size()) {
            case 0:
                return;
            case 1:
                onGoodScanned(selectedGoods.get(0));
                break;
            default:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.choose_delivery)
                        .setAdapter(new DeliveryDialogAdapter(this, selectedGoods),
                                (dialog, which) -> onGoodScanned(selectedGoods.get(which)));
                builder.create().show();
                break;
        }
    }

    private void deleteLatest() {
        File f = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera" );
        File[] files = f.listFiles();
        Arrays.sort( files, (Comparator<Object>) (o1, o2) -> {
            return Long.compare(((File) o2).lastModified(), ((File) o1).lastModified());
        });
        if(files.length > 0) files[0].delete();
    }

    public void onScanFailed() {

    }

    public void onGoodScanned(Good good) {

    }

    public void onBarcodeScanned(String barcode) {

    }

    public void onPhotoTaken(String photoPath, Bundle photoParams) {

    }

    public boolean showBackpressedDialog(){
        return false;
    }

    class DeliveryDialogAdapter extends ArrayAdapter<Good> {

        public DeliveryDialogAdapter(@NonNull Context context, @NonNull List<Good> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_good, parent, false);
            Good good = getItem(position);

            TextView tvText = v.findViewById(android.R.id.text1);
            tvText.setText(good.getDeliveryNumber());

            return v;
        }
    }
}
