package ru.a7flowers.pegorenkov.defectacts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import ru.a7flowers.pegorenkov.defectacts.adapters.DeliveryAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.Mode;
import ru.a7flowers.pegorenkov.defectacts.data.entities.Delivery;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.DeliveriesViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class MainActivity extends ItemActivity implements DeliveryAdapter.TakePhotoListener {

    private static final String DELIVERY_ID_KEY = "delivery_id";

    private DeliveriesViewModel model;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(DeliveriesViewModel.class);

        FloatingActionButton fabEdit = findViewById(R.id.fabEdit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeliveryActivity();
            }
        });

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                model.refreshData();
            }
        });
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    model.setMode(Mode.DEFECTS);
                }else if(tab.getPosition() == 1){
                    model.setMode(Mode.DIFFERENCIES);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        final RecyclerView rvDeliveries = findViewById(R.id.rvDeliveries);
        rvDeliveries.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvDeliveries.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvDeliveries.addItemDecoration(itemDecor);

        final DeliveryAdapter adapter = new DeliveryAdapter();
        adapter.setViewModel(model);
        adapter.setTakePhotoListener(this);
        rvDeliveries.setAdapter(adapter);

        model.getDeliveries().observe(this, new Observer<List<Delivery>>() {
            @Override
            public void onChanged(@Nullable List<Delivery> deliveries) {
                adapter.setItems(deliveries);
            }
        });

        model.getMode().observe(this, new Observer<Mode>() {
            @Override
            public void onChanged(@Nullable Mode mode) {
                if (mode == Mode.DEFECTS) {
                    TabLayout.Tab tab = tabs.getTabAt(0);
                    tab.select();
                }else if (mode == Mode.DIFFERENCIES) {
                    TabLayout.Tab tab = tabs.getTabAt(1);
                    tab.select();
                }
            }
        });

        model.getIsReloading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                swipeContainer.setRefreshing(isLoading);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:{
                Intent i = new Intent(MainActivity.this, PrefActivity.class);
                startActivity(i);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openDeliveryActivity(){

        String[] deliveriesIds = model.getSelectedDeliveryIds();
        if(deliveriesIds.length == 0) return;

        Mode mode = model.getMode().getValue();
        if (mode == Mode.DEFECTS) {
            Intent i = new Intent(this, DeliveryDefectActivity.class);
            i.putExtra(DeliveryDefectActivity.DELIVERY, deliveriesIds);
            startActivity(i);
        }else if(mode == Mode.DIFFERENCIES){
            Intent i = new Intent(this, DeliveryDiffActivity.class);
            i.putExtra(DeliveryDiffActivity.DELIVERY, deliveriesIds);
            startActivity(i);
        }
    }

    @Override
    public void takePhoto(Delivery delivery) {
        Bundle params = new Bundle();
        params.putString(DELIVERY_ID_KEY, delivery.getId());
        startPhoto(params);
    }

    @Override
    public void onPhotoTaken(String photoPath, Bundle photoParams) {
        String deliveryId = photoParams.getString(DELIVERY_ID_KEY);
        model.saveDeliveryPhoto(deliveryId, photoPath);
    }

}
