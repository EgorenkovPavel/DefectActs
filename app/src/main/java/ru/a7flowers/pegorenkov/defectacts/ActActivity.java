package ru.a7flowers.pegorenkov.defectacts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.adapters.DefectsAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ActViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;
import ru.a7flowers.pegorenkov.defectacts.objects.Defect;

public class ActActivity extends AppCompatActivity implements DefectsAdapter.OnDefectClickListener {

    public static final String ACT_ID = "ACT_ID";

    private ActViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(ActViewModel.class);

        Intent i = getIntent();
        if(i.hasExtra(ACT_ID)){
            String id = (String) i.getExtras().get(ACT_ID);
            model.start(id);
        }
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActActivity.this, DefectActivity.class);
                i.putExtra(DefectActivity.DELIVERY_ID, model.getDeliveryId());
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

        model.getDefects().observe(this, new Observer<List<Defect>>() {
            @Override
            public void onChanged(@Nullable List<Defect> defects) {
                adapter.setItems(defects);
            }
        });

    }

    @Override
    public void onDefectClick(Defect defect) {
        Intent i = new Intent(this, DefectActivity.class);
        i.putExtra(DefectActivity.DELIVERY_ID, model.getDeliveryId());
        i.putExtra(DefectActivity.DEFECT_KEY, defect.getKey());
        startActivity(i);
    }
}
