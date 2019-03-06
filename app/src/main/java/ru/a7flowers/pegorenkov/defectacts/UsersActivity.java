package ru.a7flowers.pegorenkov.defectacts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.a7flowers.pegorenkov.defectacts.adapters.UsersAdapter;
import ru.a7flowers.pegorenkov.defectacts.data.entities.User;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.UsersViewModel;
import ru.a7flowers.pegorenkov.defectacts.data.viewmodel.ViewModelFactory;

public class UsersActivity extends AppCompatActivity implements UsersAdapter.OnUserClickListener {

    private UsersViewModel model;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        model = ViewModelProviders.of(this, ViewModelFactory.getInstance(getApplication())).get(UsersViewModel.class);

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> model.refreshData());
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        final RecyclerView rvUsers = findViewById(R.id.rvUsers);
        rvUsers.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvUsers.addItemDecoration(itemDecor);

        final UsersAdapter adapter = new UsersAdapter();
        adapter.setOnUserClickListener(this);
        rvUsers.setAdapter(adapter);

        model.getUsers().observe(this, users -> {if(users != null) adapter.setItems(users);});

        model.getIsReloading().observe(this, isLoading -> {if(isLoading != null)swipeContainer.setRefreshing(isLoading);});

        model.isActualVersion().observe(this,
                isActualVersion -> {
            if(isActualVersion != null && !isActualVersion){
                showVersionDialog();
            }
        });
    }

    private void showVersionDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UsersActivity.this)
                .setTitle("Version error")
                .setMessage("Version of app don't matches with server version. Please upgrade the app")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> UsersActivity.this.finish());

        dialogBuilder.show();
    }

    @Override
    public void onUserClick(User user) {
        model.setCurrentUser(user);

        Intent i = new Intent(this, DeliveriesActivity.class);
        startActivity(i);
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
                Intent i = new Intent(UsersActivity.this, PrefActivity.class);
                startActivity(i);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
