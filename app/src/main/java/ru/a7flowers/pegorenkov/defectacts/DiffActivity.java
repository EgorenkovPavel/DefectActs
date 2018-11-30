package ru.a7flowers.pegorenkov.defectacts;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private TextView tvNewPhotoCount;
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
                if(count == null || count == 0){
                    tvNewPhotoCount.setText("");
                }else {
                    tvNewPhotoCount.setText("+" + String.valueOf(count));
                }
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

        model.getGoodDiffs().observe(this, new Observer<List<Diff>>() {
            @Override
            public void onChanged(@Nullable List<Diff> diffs) {
                if(diffs == null || diffs.isEmpty()) return;
                showChooseDiffDialog(diffs);
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
            tvPhotoCount.setText(diff.getPhotoQuantity() == 0 ? "" : String.valueOf(diff.getPhotoQuantity()));

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
        tvNewPhotoCount = findViewById(R.id.tvNewPhotoCount);

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

    private void showChooseDiffDialog(final List<Diff> diffs){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_position_dialog_title)
                .setAdapter(new DiffsDialogAdapter(this, diffs),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                model.setDiff(diffs.get(i));
                            }
                        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        acSearch.setText("");
                        acSearch.requestFocus();
                    }
                })
                .setNeutralButton(getString(R.string.btn_create_new), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        model.createNewDiffByGood();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        acSearch.setText("");
                        acSearch.requestFocus();
                    }
                });
        builder.show();
    }

    class DiffsDialogAdapter extends ArrayAdapter<Diff> {

        public DiffsDialogAdapter(@NonNull Context context, @NonNull List<Diff> objects) {
            super(context, R.layout.item_diff, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diff, parent, false);

            Diff diff = getItem(position);

            TextView tvSeries = v.findViewById(R.id.tvSeries);
            TextView tvGood = v.findViewById(R.id.tvGood);
            TextView tvSuplier = v.findViewById(R.id.tvSuplier);
            TextView tvCountry = v.findViewById(R.id.tvCountry);
            TextView tvQuantity = v.findViewById(R.id.tvQuantity);
            TextView tvPhotoQuantity = v.findViewById(R.id.tvPhotoQuantity);

            tvSeries.setText(diff.getSeries());
            tvGood.setText(diff.getTitle());
            tvSuplier.setText(diff.getSuplier());
            tvCountry.setText(diff.getCountry());
            tvQuantity.setText(String.valueOf(diff.getQuantity()));
            tvPhotoQuantity.setText(String.valueOf(diff.getPhotoQuantity()));

            return v;
        }
    }


}
