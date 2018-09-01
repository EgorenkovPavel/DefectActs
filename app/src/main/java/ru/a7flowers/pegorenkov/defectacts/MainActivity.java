package ru.a7flowers.pegorenkov.defectacts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.adapters.DeliveryAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DeliveriesViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class MainActivity extends AppCompatActivity{

    @SuppressWarnings("FieldCanBeLocal")
    private DeliveriesViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(DeliveriesViewModel.class);

        RecyclerView rvDeliveries = findViewById(R.id.rvDeliveries);
        rvDeliveries.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvDeliveries.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvDeliveries.addItemDecoration(itemDecor);

        final DeliveryAdapter adapter = new DeliveryAdapter();
        adapter.setViewModel(model);

        rvDeliveries.setAdapter(adapter);

        model.getDeliveries().observe(this, new Observer<List<Delivery>>() {
            @Override
            public void onChanged(@Nullable List<Delivery> deliveries) {
                adapter.setItems(deliveries);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deliveries_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:{
                openDeliveryActivity();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDeliveryActivity(){

        String[] deliveriesIds = model.getSelectedDeliveryIds();
        if(deliveriesIds.length == 0) return;

        Intent i = new Intent(this, DeliveryActivity.class);
        i.putExtra(DeliveryActivity.DELIVERY, deliveriesIds);
        startActivity(i);
    }

}
