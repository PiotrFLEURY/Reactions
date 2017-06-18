package fr.piotr.reactions;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import fr.piotr.reactions.adapters.AddressAdapter;
import fr.piotr.reactions.dialogs.LocationPicker;
import fr.piotr.reactions.utils.LocationConverter;

public class AddressListActivity extends AppCompatActivity {

    public static final String INTENT_MODE = "INTENT_MODE";

    public static final String MODE_SELECTION = "MODE_SELECTION";

    public static final String EVENT_LOCATION_PICKED_UP = "EVENT_LOCATION_PICKED_UP";
    public static final String EXTRA_LOCATION_ADDRESS = "EXTRA_LOCATION_ADDRESS";

    public static final String EVENT_LOCATION_SELECTED = "EVENT_LOCATION_SELECTED";
    public static final String EXTRA_LOCATION_UUID = "EXTRA_LOCATION_UUID";

    public static final int LOCATION_PERMISSION_REQUEST = 1;

    RecyclerView rvAddressList;
    AddressAdapter addressAdapter;

    IntentFilter intentFilter = new IntentFilter() {{
        addAction(EVENT_LOCATION_PICKED_UP);
        addAction(EVENT_LOCATION_SELECTED);
    }};

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EVENT_LOCATION_PICKED_UP:
                    onAddressPickedUp((Address) intent.getParcelableExtra(EXTRA_LOCATION_ADDRESS));
                    break;
                case EVENT_LOCATION_SELECTED:
                    onLocationSelected(intent);
                    break;
            }
        }
    };

    private void onLocationSelected(Intent intent) {
        Intent data = new Intent();
        data.putExtra(EXTRA_LOCATION_UUID, intent.getStringExtra(EXTRA_LOCATION_UUID));
        setResult(EditRuleActivity.SELECT_LOCATION_REQUEST, data);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
        ((AddressAdapter)rvAddressList.getAdapter()).setCurrentLocation(LocationConverter.getCurrentLocation(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_address);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickupLocation();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvAddressList = (RecyclerView) findViewById(R.id.rv_address_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvAddressList.setHasFixedSize(true);

        // use a linear layout manager
        rvAddressList.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        addressAdapter = new AddressAdapter(this, LocationConverter.getCurrentLocation(this));
        String intentMode = getIntent().getStringExtra(INTENT_MODE);
        if(MODE_SELECTION.equals(intentMode)){
            addressAdapter.setSelectionMode(true);
            setTitle(getString(R.string.title_activity_address_list_selection_mode));
        }
        rvAddressList.setAdapter(addressAdapter);

    }

    private void pickupLocation() {
        if(!requestLocationPermission()) {
            LocationPicker locationPicker = new LocationPicker(this);
            locationPicker.show();
        }
    }

    private void onAddressPickedUp(Address address) {
        ReactionsApplication.getAddressManager().add(address);
        addressAdapter.refresh();
    }

    private boolean requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==LOCATION_PERMISSION_REQUEST
                && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            pickupLocation();
        }
    }

}
