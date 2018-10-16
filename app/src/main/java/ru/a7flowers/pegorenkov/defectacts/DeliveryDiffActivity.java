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
import ru.a7flowers.pegorenkov.defectacts.adapters.DiffsAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.network.DefectWithReasons;
import ru.a7flowers.pegorenkov.defectacts.data.network.Diff;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DeliveryDefectViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DeliveryDiffViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class DeliveryDiffActivity extends AppCompatActivity implements DiffsAdapter.OnDiffClickListener {

    public static final String DELIVERY = "delivery";

    private DeliveryDiffViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_diff);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(DeliveryDiffViewModel.class);

        Intent i = getIntent();
        if(i.hasExtra(DELIVERY)){
            String[] deliveries = i.getExtras().getStringArray(DELIVERY);
            model.start(deliveries);
        }
        
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DeliveryDiffActivity.this, DefectActivity.class);
                i.putExtra(DefectActivity.DELIVERY, model.getDeliveryIds());
                startActivity(i);
            }
        });

        RecyclerView rvDefects = findViewById(R.id.rvDefects);

        rvDefects.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvDefects.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvDefects.addItemDecoration(itemDecoration);

        final DiffsAdapter adapter = new DiffsAdapter();
        adapter.setOnDiffClickListener(this);

        rvDefects.setAdapter(adapter);

        model.getDiffs().observe(this, new Observer<List<Diff>>() {
            @Override
            public void onChanged(@Nullable List<Diff> diffs) {
                adapter.setItems(diffs);
            }
        });

    }

    @Override
    public void onDiffClick(Diff diff) {
        Intent i = new Intent(this, DefectActivity.class);
        i.putExtra(DefectActivity.DELIVERY, model.getDeliveryIds());
        i.putExtra(DefectActivity.DEFECT, diff.getId());
        startActivity(i);
    }
}
