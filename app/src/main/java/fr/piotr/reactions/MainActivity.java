package fr.piotr.reactions;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import fr.piotr.reactions.adapters.RulesAdapter;
import fr.piotr.reactions.registry.AppActions;
import fr.piotr.reactions.registry.PossibleEvents;
import fr.piotr.reactions.registry.PossibleReactions;
import fr.piotr.reactions.services.ReactionsService;
import fr.piotr.reactions.utils.LocationConverter;

public class MainActivity extends AppCompatActivity implements AppActions, PossibleEvents, PossibleReactions, NavigationView.OnNavigationItemSelectedListener {

    public static final int READ_STORAGE_PERMISSION_REQUEST = 4;
    public static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;
    public static final int ACCESS_LOCATION_PERMISSION_REQUEST = 6;

    public static final String EVENT_REFRESH = "EVENT_REFRESH";

    Switch mainSwitch;
    FloatingActionButton addReaction;
    RecyclerView mRecyclerView;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView appBarLeftIcon;

    IntentFilter intentFilter = new IntentFilter() {{
        addAction(EVENT_REFRESH);
    }};

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EVENT_REFRESH:
                    refresh();
                    break;
            }
        }
    };

    private void refresh() {
        ((RulesAdapter)mRecyclerView.getAdapter()).setCurrentLocation(getCurrentLocation());
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        View appBarLayout = LayoutInflater.from(this).inflate(R.layout.app_bar, null);
        appBarLeftIcon = (ImageView) appBarLayout.findViewById(R.id.app_bar_left_icon);
        getSupportActionBar().setCustomView(appBarLayout);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.left_drawer);
        mNavigationView.setNavigationItemSelectedListener(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });

        mainSwitch = (Switch) findViewById(R.id.main_switch);
        mainSwitch.setChecked(ReactionsService.running.get());

        addReaction = (FloatingActionButton) findViewById(R.id.addReaction);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvRules);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(new RulesAdapter(this, getCurrentLocation()));

    }

    private Location getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_PERMISSION_REQUEST);
            return null;
        }
        return LocationConverter.getCurrentLocation(this);
    }

    private void toggleDrawer() {
        if(mDrawerLayout.isDrawerOpen(mNavigationView)){
            appBarLeftIcon.setImageResource(R.drawable.ic_menu);
            mDrawerLayout.closeDrawers();
        } else {
            appBarLeftIcon.setImageResource(R.drawable.ic_arrow_left);
            mDrawerLayout.openDrawer(mNavigationView);
        }
    }

    private boolean requestWriteStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION_REQUEST);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleService(null);
            }
        });
        addReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReaction(null);
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
        refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainSwitch.setOnClickListener(null);
        addReaction.setOnClickListener(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    public void toggleService(View v) {
        if (ReactionsService.running.get()) {
            ReactionsApplication.getReactionsManager().setLastReaction(null);
            stopService(new Intent(this, ReactionsService.class));
        } else {
            startService(new Intent(this, ReactionsService.class));
        }
    }

    private void addReaction(View view) {

        if(requestWriteStoragePermission()
                || requestReadStoragePermission()){
            return;
        }

        startActivity(new Intent(this, EditRuleActivity.class));

    }

    private boolean requestReadStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.drawer_menu_adresses:
                startActivity(new Intent(this, AddressListActivity.class));
                toggleDrawer();
                return true;
        }
        return false;
    }
}
