package ru.a7flowers.pegorenkov.defectacts;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.adapters.GoodsSearchAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Good;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DiffViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class DiffActivity extends ItemActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    private static final int RC_BARCODE_CAPTURE = 9001;
    public static final String DELIVERY = "delivery";
    public static final String DIFF = "diff";

    private DiffViewModel model;
    private AutoCompleteTextView acSearch;
    private TextView tvSeries;
    private TextView tvTitle;
    private TextView tvSuplier;
    private TextView tvCountry;
    private TextView tvDelivery;
    private EditText etComment;
    private EditText etAmount;
    private GoodsSearchAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(DiffViewModel.class);

        findViews();
        
        Intent i = getIntent();
        if (i.hasExtra(DELIVERY)){
            String[] deliveryIds = i.getExtras().getStringArray(DELIVERY);
            if(i.hasExtra(DIFF)){
                String diffId = i.getStringExtra(DIFF);
                model.start(deliveryIds, diffId);
            }else{
                model.start(deliveryIds);
            }
        }

        model.getGoods().observe(this, new Observer<List<Good>>() {
            @Override
            public void onChanged(@Nullable List<Good> goods) {
                adapter.clear();
                if (goods != null) adapter.setItems(goods);
            }
        });

        model.getDiffAmount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer value) {
                String text = String.valueOf(value);
                if(!text.equals(etAmount.getText().toString())) {
                    etAmount.setText(text);
                    etAmount.setSelection(etAmount.getText().length());
                }
            }
        });

        model.getDiffComment().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String comment) {
                String text = String.valueOf(comment);
                if(!text.equals(etComment.getText().toString())) {
                    etComment.setText(comment);
                    etComment.setSelection(etComment.getText().length());
                }
            }
        });
        model.getDiffSeries().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String series) {
                tvSeries.setText(series);
            }
        });
        model.getDiffTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvTitle.setText(s);
            }
        });
        model.getDiffSuplier().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvSuplier.setText(s);
            }
        });
        model.getDiffCountry().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvCountry.setText(s);
            }
        });
        model.getDiffDelivery().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvDelivery.setText(s);
            }
        });
    }

    private void findViews() {

        View includeAmount = findViewById(R.id.includeAmount);
        TextView titleAmount = includeAmount.findViewById(R.id.lblTitle);
        titleAmount.setText(R.string.amount);

        acSearch = findViewById(R.id.acSearch);
        tvSeries = findViewById(R.id.tvSeries);
        tvTitle = findViewById(R.id.tvTitle);
        tvSuplier = findViewById(R.id.tvSuplier);
        tvCountry = findViewById(R.id.tvCountry);
        tvDelivery = findViewById(R.id.tvDelivery);
        etComment = findViewById(R.id.etComment);

        etAmount = includeAmount.findViewById(R.id.etAmount);

        ImageButton btnAmountInc = includeAmount.findViewById(R.id.btnInc);
        ImageButton btnAmountDec = includeAmount.findViewById(R.id.btnDec);
        ImageButton ibPhoto = findViewById(R.id.ibPhoto);
        ImageButton ibNext = findViewById(R.id.ibNext);
        ImageButton ibBarcode = findViewById(R.id.ibBarcode);
        ImageButton ibClear = findViewById(R.id.btnClear);

        acSearch.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        acSearch.setKeyListener(DigitsKeyListener.getInstance("0123456789@#"));

        adapter = new GoodsSearchAdapter(this);
        adapter.setCallback(new GoodsSearchAdapter.GoodFoundCallback() {
            @Override
            public void onGoodFounded(Good good) {
                model.setGood(good);
            }
        });
        acSearch.setAdapter(adapter);
        acSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Good good = (Good) adapterView.getAdapter().getItem(i);
                model.setGood(good);
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

        btnAmountInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.incAmount();
            }
        });

        btnAmountDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.decAmount();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.saveDiff();
                acSearch.setText("");
                acSearch.requestFocus();
            }
        });

        ibBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarcode(model.getGoods().getValue());

            }
        });

        ibPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhoto();
                }
        });

        ibClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acSearch.setText("");
            }
        });
    }

    @Override
    public void onBarcodeScanned(String barcode) {
        acSearch.setText(barcode);
        Toast.makeText(this, barcode, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onScanFailed() {
        Toast.makeText(this, R.string.barcode_failure, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGoodScanned(Good good) {
        model.setGood(good);
    }

    @Override
    public void onPhotoTaken(String photoPath) {
        model.setPhotoPath(photoPath);
    }


}
