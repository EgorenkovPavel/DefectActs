package ru.a7flowers.pegorenkov.defectacts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.adapters.DefectsAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Defect;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DeliveryViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;
import ru.a7flowers.pegorenkov.defectacts.objects.DefectGood;

public class DeliveryActivity extends AppCompatActivity implements DefectsAdapter.OnDefectClickListener {

    public static final String DELIVERY = "delivery";

    private DeliveryViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(DeliveryViewModel.class);

        Intent i = getIntent();
        if(i.hasExtra(DELIVERY)){
            Delivery delivery = (Delivery) i.getExtras().getSerializable(DELIVERY);
            model.start(delivery);
        }
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DeliveryActivity.this, DefectActivity.class);
                i.putExtra(DefectActivity.DELIVERY, model.getDelivery());
                startActivity(i);
            }
        });

        RecyclerView rvDefects = findViewById(R.id.rvDefects);

        rvDefects.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvDefects.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvDefects.addItemDecoration(itemDecoration);

        final DefectsAdapter adapter = new DefectsAdapter();
        adapter.setOndefectClickListener(this);

        rvDefects.setAdapter(adapter);

        model.getDefects().observe(this, new Observer<List<DefectGood>>() {
            @Override
            public void onChanged(@Nullable List<DefectGood> defects) {
                adapter.setItems(defects);
            }
        });

    }

    @Override
    public void onDefectClick(Defect defect) {
        Intent i = new Intent(this, DefectActivity.class);
        i.putExtra(DefectActivity.DELIVERY, model.getDelivery());
        i.putExtra(DefectActivity.DEFECT, defect);
        startActivity(i);
    }
}