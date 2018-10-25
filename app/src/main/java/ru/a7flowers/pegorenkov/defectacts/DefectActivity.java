package ru.a7flowers.pegorenkov.defectacts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.io.Serializable;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.adapters.GoodsSearchAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DefectViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class DefectActivity extends ItemActivity {

    public static final String DELIVERY = "delivery_id";
    public static final String DEFECT = "defect_key";

    private static final int SELECT_REASON = 47;
    public static final String SELECTED_REASONS = "selected_reasons";

    private DefectViewModel model;

    private GoodsSearchAdapter adapter;

    private EditText etAmount;
    private EditText etWriteoff;
    private TextView tvReasons;
    private EditText etComment;
    private TextView tvSeries;
    private TextView tvTitle;
    private TextView tvSuplier;
    private TextView tvCountry;
    private TextView tvDelivery;
    private AutoCompleteTextView acSearch;

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

        model.getGoods().observe(this, new Observer<List<Good>>() {
            @Override
            public void onChanged(@Nullable List<Good> goods) {
                adapter.clear();
                if (goods != null) adapter.setItems(goods);
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

        model.getDefectWriteoff().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer value) {
                String text = String.valueOf(value);
                if(!text.equals(etWriteoff.getText().toString())) {
                    etWriteoff.setText(text);
                    etWriteoff.setSelection(etWriteoff.getText().length());
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
                tvSeries.setText(series);
            }
        });
        model.getDefectTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvTitle.setText(s);
            }
        });
        model.getDefectSuplier().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvSuplier.setText(s);
            }
        });
        model.getDefectCountry().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvCountry.setText(s);
            }
        });
        model.getDefectDelivery().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvDelivery.setText(s);
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

        View includeAmount = findViewById(R.id.includeAmount);
        View includeWriteoff = findViewById(R.id.includeWriteoff);
        TextView titleAmount = includeAmount.findViewById(R.id.lblTitle);
        titleAmount.setText(R.string.amount);
        TextView titleWriteoff = includeWriteoff.findViewById(R.id.lblTitle);
        titleWriteoff.setText(R.string.writeoff);

        acSearch = findViewById(R.id.acSearch);
        tvSeries = findViewById(R.id.tvSeries);
        tvTitle = findViewById(R.id.tvTitle);
        tvSuplier = findViewById(R.id.tvSuplier);
        tvCountry = findViewById(R.id.tvCountry);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvReasons = findViewById(R.id.tvReasons);
        etComment = findViewById(R.id.etComment);

        etAmount = includeAmount.findViewById(R.id.etAmount);
        etWriteoff = includeWriteoff.findViewById(R.id.etAmount);

        ImageButton btnAmountInc = includeAmount.findViewById(R.id.btnInc);
        ImageButton btnAmountDec = includeAmount.findViewById(R.id.btnDec);
        ImageButton btnWriteoffInc = includeWriteoff.findViewById(R.id.btnInc);
        ImageButton btnWriteoffDec = includeWriteoff.findViewById(R.id.btnDec);
        ImageButton ibPhoto = findViewById(R.id.ibPhoto);
        ImageButton ibNext = findViewById(R.id.ibNext);
        ImageButton ibBarcode = findViewById(R.id.ibBarcode);
        ImageButton ibClear = findViewById(R.id.btnClear);

        acSearch.setThreshold(3);
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

        etWriteoff.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = String.valueOf(etWriteoff.getText());
                int value = text.isEmpty() ? 0 : Integer.valueOf(text);
                model.setWriteoff(value);
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

        btnWriteoffInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.incWriteoff();
            }
        });

        btnWriteoffDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.decWriteoff();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.saveDefect();
                acSearch.setText("");
                acSearch.requestFocus();
            }
        });

        ibBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarcode();
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

    private void chooseReason(){
        Intent i = new Intent(this, ReasonsActivity.class);
        i.putExtra(SELECTED_REASONS, (Serializable) model.getDefectReasonsList());
        startActivityForResult(i, SELECT_REASON);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(requestCode == SELECT_REASON && resultCode == RESULT_OK){
            if(intent.hasExtra(SELECTED_REASONS)) {
                List<Reason> list = (List<Reason>) intent.getExtras().getSerializable(SELECTED_REASONS);
                model.setDefectReasons(list);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void onBarcodeScanned(String barcode) {
        acSearch.setText(barcode);
        List<Good> goods = model.findGoodsByBarcode(barcode);
        chooseGood(goods);
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