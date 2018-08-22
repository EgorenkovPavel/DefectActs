package ru.a7flowers.pegorenkov.defectacts;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.a7flowers.pegorenkov.defectacts.objects.Good;

public class DefectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner sGoods = findViewById(R.id.sGoods);
        TextView tvReasons = findViewById(R.id.tvReasons);
        EditText etAmount = findViewById(R.id.etAmount);
        EditText etComment = findViewById(R.id.etComment);
        Button btnInc = findViewById(R.id.btnInc);
        Button btnDec = findViewById(R.id.btnDec);
        ImageButton ibPhoto = findViewById(R.id.ibPhoto);
        ImageButton ibNext = findViewById(R.id.ibNext);
        ImageButton ibBarcode = findViewById(R.id.ibBarcode);

        Intent i = getIntent();

        ArrayList<Good> mGoods = new ArrayList<>();
        mGoods.add(new Good("1", "55000002123123", "Rose", "Boston", "Russia", 21));
        mGoods.add(new Good("1", "55000002123123", "Rose", "Boston", "Russia", 22));
        mGoods.add(new Good("1", "55000002123123", "Rose", "Boston", "Russia", 23));
        mGoods.add(new Good("1", "55000002123123", "Rose", "Boston", "Russia", 24));

        ArrayAdapter adapter = new GoodsAdapter(this);
        adapter.addAll(mGoods);

        sGoods.setAdapter(adapter);
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
            tvSuplier.setText(good.getSupier());
            tvCounty.setText(good.getCountry());
            tvAmount.setText(String.valueOf(good.getQuantity()));

            return v;
        }
    }
}
