package ru.a7flowers.pegorenkov.defectacts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.adapters.ReasonsAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ReasonsViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Reason;

public class ReasonsActivity extends AppCompatActivity implements ReasonsAdapter.OnReasonClickListener {

    private ReasonsViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reasons);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(ReasonsViewModel.class);

        Button btnOk = findViewById(R.id.btnOk);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra(DefectActivity.SELECTED_REASONS, (Serializable) model.getDefectReasons().getValue());

                setResult(RESULT_OK, i);
                finish();
            }
        });

        RecyclerView rvReasons = findViewById(R.id.rvReasons);

        rvReasons.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvReasons.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvReasons.addItemDecoration(itemDecoration);

        final ReasonsAdapter adapter = new ReasonsAdapter();

        adapter.setOnReasonClickListener(this);

        rvReasons.setAdapter(adapter);

        Intent i = getIntent();
        if(i.hasExtra(DefectActivity.SELECTED_REASONS)) {
            List<Reason> list = (List<Reason>) i.getExtras().get(DefectActivity.SELECTED_REASONS);
            model.setDefectReasons(list);
        }

        model.getReasons().observe(this, new Observer<List<Reason>>() {
            @Override
            public void onChanged(@Nullable List<Reason> reasons) {
                adapter.setItems(reasons);
            }
        });
        model.getDefectReasons().observe(this, new Observer<List<Reason>>() {
            @Override
            public void onChanged(@Nullable List<Reason> reasons) {
                adapter.setSelectedItems(reasons);
            }
        });
    }

    @Override
    public void onReasonClick(Reason reason) {
        model.selectReason(reason);
    }
}
