package ru.a7flowers.pegorenkov.defectacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import ru.a7flowers.pegorenkov.defectacts.adapters.DefectsAdapter;
import ru.a7flowers.pegorenkov.defectacts.objects.Defect;

public class ActActivity extends AppCompatActivity implements DefectsAdapter.OnDefectClickListener {

    public static final String ACT_ID = "ACT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        if(i.hasExtra(ACT_ID)){
            loadAct();
        }else{
            createAct();
        }
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView rvDefects = findViewById(R.id.rvDefects);

        rvDefects.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvDefects.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvDefects.addItemDecoration(itemDecoration);

        ArrayList<Defect> defects = new ArrayList<>();
        defects.add(new Defect("1", "55000323423423", "Rose", "OZ", "Belgium", 43, 5));
        defects.add(new Defect("1", "55000323423423", "Rose", "OZ", "Belgium", 43, 5));
        defects.add(new Defect("1", "55000323423423", "Rose", "OZ", "Belgium", 43, 5));

        DefectsAdapter adapter = new DefectsAdapter();
        adapter.setOndefectClickListener(this);
        adapter.setItems(defects);

        rvDefects.setAdapter(adapter);
    }

    private void createAct() {
    }

    private void loadAct() {
    }

    @Override
    public void onDefectClick(Defect defect) {

    }
}
