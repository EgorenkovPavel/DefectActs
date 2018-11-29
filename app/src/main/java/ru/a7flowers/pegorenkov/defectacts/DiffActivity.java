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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.views.AmountView;
import ru.a7flowers.pegorenkov.defectacts.views.EditTextDropdown;
import ru.a7flowers.pegorenkov.defectacts.views.EditTextDropdown.TextChangeListener;
import ru.a7flowers.pegorenkov.defectacts.adapters.GoodsSearchAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;
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
    private TextView tvDeliveryAmount;
    private EditText etComment;
    private AmountView etAmount;
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

        if (savedInstanceState == null) {
            Intent i = getIntent();
            if (i.hasExtra(DELIVERY)) {
                String[] deliveryIds = i.getExtras().getStringArray(DELIVERY);
                if (i.hasExtra(DIFF)) {
                    String diffId = i.getStringExtra(DIFF);
                    model.start(deliveryIds, diffId);
                } else {
                    model.start(deliveryIds);
                }
            }
        }else{
            model.restoreState();
        }

        model.getGoods().observe(this, new Observer<List<Good>>() {
            @Override
            public void onChanged(@Nullable List<Good> goods) {
                adapter.clear();
                if (goods != null) adapter.setItems(goods);
            }
        });

        model.getPhotoCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer count) {
                tvPhotoCount.setText(String.valueOf(count));
            }
        });

        model.getDiff().observe(this, new Observer<Diff>() {
            @Override
            public void onChanged(@Nullable Diff diff) {
                if (diff == null)
                    init(new Diff());
                else
                    init(diff);
            }
        });
    }

    private void init(Diff diff){
        etAmount.setValue(diff.getQuantity());

        fillEditText(etComment, diff.getComment());

        if((String.valueOf(tvSeries.getText())).equals(diff.getSeries())){

        }else {
            tvSeries.setText(diff.getSeries());
            tvTitle.setText(diff.getTitle());
            tvSuplier.setText(diff.getSuplier());
            tvCountry.setText(diff.getCountry());
            tvDelivery.setText(diff.getDeliveryNumber());
            tvDeliveryAmount.setText(String.valueOf(diff.getDeliveryQuantity()));

            List<ValueData> data = new ArrayList<>();

            addValueData(data, diff.getListLength(), R.string.length,
                    diff.getLength(),
                    new TextChangeListener() {
                        @Override
                        public void onTextChanged(String value) {
                            model.setDiffLength(Integer.parseInt(value));
                        }
                    });
            addValueData(data, diff.getListDiameter(), R.string.diameter,
                    diff.getDiameter(),
                    new TextChangeListener() {
                        @Override
                        public void onTextChanged(String value) {
                            model.setDiffDiameter(Float.parseFloat(value));
                        }
                    });
            addValueData(data, diff.getListBulk(), R.string.bulk,
                    diff.getBulk(),
                    new TextChangeListener() {
                        @Override
                        public void onTextChanged(String value) {
                            model.setDiffBulk(Integer.parseInt(value));
                        }
                    });
            addValueData(data, diff.getListBudgeonAmount(), R.string.budgeonAmount,
                    diff.getBudgeonAmount(),
                    new TextChangeListener() {
                        @Override
                        public void onTextChanged(String value) {
                            model.setDiffBudgeonAmount(Integer.parseInt(value));
                        }
                    });
            addValueData(data, diff.getListWeigth(), R.string.weigth,
                    diff.getWeigth(),
                    new TextChangeListener() {
                        @Override
                        public void onTextChanged(String value) {
                            model.setDiffWeigth(Integer.parseInt(value));
                        }
                    });

            initValues(data);
        }
    }

    private void fillEditText(EditText view, String text){
        if (text == null) {
            view.setText("");
            return;
        }
        if(!text.equals(view.getText().toString())) {
            view.setText(text);
            view.setSelection(view.getText().length());
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

        acSearch = findViewById(R.id.acSearch);
        tvSeries = findViewById(R.id.tvSeries);
        tvTitle = findViewById(R.id.tvTitle);
        tvSuplier = findViewById(R.id.tvSuplier);
        tvCountry = findViewById(R.id.tvCountry);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvDeliveryAmount = findViewById(R.id.tvDeliveryAmount);
        etComment = findViewById(R.id.etComment);
        tvPhotoCount = findViewById(R.id.tvPhotoCount);

        etAmount = findViewById(R.id.includeAmount);
        etAmount.setTitle(getString(R.string.amount));
        etAmount.setListener(new AmountView.ValueChangeListener() {
            @Override
            public void onValueChange(int value) {
                model.setAmount(value);
            }
        });

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
                startPhoto(null);
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
    public void onPhotoTaken(String photoPath, Bundle photoParams) {
        model.setPhotoPath(photoPath);
    }

    //TODO add dialog to itemActivity
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
                startPhoto(null);
            }
        });
        return builder.create();
    }

    private void saveDiff(){
        model.saveDiff();
        acSearch.setText("");
        acSearch.requestFocus();
    }

    @Override
    public boolean showBackpressedDialog() {
        return model.showBackpressedDialog();
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
