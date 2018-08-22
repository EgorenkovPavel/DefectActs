package ru.a7flowers.pegorenkov.defectacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.a7flowers.pegorenkov.defectacts.adapters.DeliveryAdapter;
import ru.a7flowers.pegorenkov.defectacts.objects.Delivery;

public class MainActivity extends AppCompatActivity implements DeliveryAdapter.OnDeliveryClickListener{

    private RecyclerView rvDeliveries;
    private ArrayList<Delivery> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvDeliveries = findViewById(R.id.rvDeliveries);
        rvDeliveries.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvDeliveries.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvDeliveries.addItemDecoration(itemDecor);

        items.add(new Delivery("1", "CTQ342342", new Date(), 23, true));
        items.add(new Delivery("1", "CTQ342342", new Date(), 23, false));
        items.add(new Delivery("1", "CTQ342342", new Date(), 23, true));

        DeliveryAdapter adapter = new DeliveryAdapter();
        adapter.setItems(items);
        adapter.setOnItemClickListener(this);

        rvDeliveries.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDeliveryClick(Delivery delivery) {
        Intent i = new Intent(this, ActActivity.class);
        i.putExtra(ActActivity.ACT_ID, delivery.getActId());
        startActivity(i);
    }

}
