package ru.a7flowers.pegorenkov.defectacts;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.EditTextDropdown.TextChangeListener;
import ru.a7flowers.pegorenkov.defectacts.adapters.GoodsSearchAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.network.Good;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DiffViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class DiffActivity extends ItemActivity {

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
    private TextView tvPhotoCount;
    private EditTextDropdown[] values = new EditTextDropdown[5];

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

        model.getDiffGood().observe(this, new Observer<Good>() {
            @Override
            public void onChanged(@Nullable Good good) {
                init(good);
            }
        });

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

        model.getPhotoCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer count) {
                tvPhotoCount.setText(String.valueOf(count));
            }
        });
    }

    private void init(Good good){
        if (good != null) {
            tvSeries.setText(good.getSeries());
            tvTitle.setText(good.getGood());
            tvSuplier.setText(good.getSuplier());
            tvCountry.setText(good.getCountry());
            tvDelivery.setText(good.getDeliveryNumber());

            List<ValueData> data = new ArrayList<>();

            addValueData(data, good.getLength(), R.string.length,
                    model.getDiffLength(),
                    new TextChangeListener() {
                @Override
                public void onTextChanged(String value) {
                    model.setDiffLength(Integer.parseInt(value));
                }
            });
            addValueData(data, good.getDiameter(), R.string.diameter,
                    model.getDiffDiameter(),
                    new TextChangeListener() {
                @Override
                public void onTextChanged(String value) {
                    model.setDiffDiameter(Float.parseFloat(value));
                }
            });
            addValueData(data, good.getBulk(), R.string.bulk,
                    model.getDiffBulk(),
                    new TextChangeListener() {
                @Override
                public void onTextChanged(String value) {
                    model.setDiffBulk(Integer.parseInt(value));
                }
            });
            addValueData(data, good.getBudgeonAmount(), R.string.budgeonAmount,
                    model.getDiffBudgeonAmount(),
                    new TextChangeListener() {
                @Override
                public void onTextChanged(String value) {
                    model.setDiffBudgeonAmount(Integer.parseInt(value));
                }
            });
            addValueData(data, good.getWeigth(), R.string.weigth,
                    model.getDiffWeigth(),
                    new TextChangeListener() {
                @Override
                public void onTextChanged(String value) {
                    model.setDiffWeigth(Integer.parseInt(value));
                }
            });

            initValues(data);

        }else{
            tvSeries.setText("");
            tvTitle.setText("");
            tvSuplier.setText("");
            tvCountry.setText("");
            tvDelivery.setText("");

            initValues(null);
        }
    }

    private <T extends Number> void addValueData(List<ValueData> data, List<T> list,
                              int resLabel,
                              T value,
                              TextChangeListener listener){
        if (list != null && !list.isEmpty()) {
            data.add(new ValueData<T>(resLabel, list, value, listener));
        }
    }

    private void initValues(List<ValueData> data){
        int i = 0;

        if(data != null) {
            for (; i < data.size(); i++) {
                EditTextDropdown view = values[i];
                view.setVisibility(View.VISIBLE);
                view.setTitle(data.get(i).getLabel());
                view.setList(data.get(i).getValues());
                view.setValue(data.get(i).getValue());
                view.setTextChangeListener(data.get(i).getListener());
            }
        }

        for (;i<values.length;i++){
            View view = values[i];
            view.setVisibility(View.GONE);
        }
    }

    private void findViews() {

        values[0] = findViewById(R.id.includeNum1);
        values[1] = findViewById(R.id.includeNum2);
        values[2] = findViewById(R.id.includeNum3);
        values[3] = findViewById(R.id.includeNum4);
        values[4] = findViewById(R.id.includeNum5);

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
        tvPhotoCount = findViewById(R.id.tvPhotoCount);

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

                if (model.getPhotoCount().getValue() == 0){
                    getDialogNoPhoto().show();
                }else{
                    saveDiff();
                }
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

    private Dialog getDialogNoPhoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DiffActivity.this);
        builder.setMessage(R.string.dialog_no_photo)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveDiff();
                    }
                })
        .setNeutralButton(R.string.photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startPhoto();
            }
        });
        return builder.create();
    }

    private void saveDiff(){
        model.saveDiff();
        acSearch.setText("");
        acSearch.requestFocus();
    }

    private class ValueData<T extends Number>{
        private List<T> values;
        private int label;
        private TextChangeListener listener;
        private T value;

        public ValueData(int label, List<T> values, T value, TextChangeListener listener) {
            this.values = values;
            this.label = label;
            this.value = value;
            this.listener = listener;
        }

        public List<T> getValues() {
            return values;
        }

        public int getLabel() {
            return label;
        }

        public TextChangeListener getListener() {
            return listener;
        }

        public T getValue() {
            return value;
        }
    }

}
