package fr.piotr.reactions.events.position;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.persistence.AddressReference;
import fr.piotr.reactions.reactions.Reaction;
import fr.piotr.reactions.registry.EventsRegistry;
import fr.piotr.reactions.utils.LocationConverter;

/**
 * Created by piotr_000 on 27/11/2016.
 *
 */

public class PositionListener implements Event, LocationListener {

    private Runnable startRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                onLocationChanged(locationManager.getLastKnownLocation(provider));
            } catch (SecurityException e){
                Log.e(getClass().getSimpleName(), e.getMessage());
            }
        }
    };

    private Handler handler = new Handler();

    private static final long MINIMUM_TIME_MILLIS = 300000;
    private static final int MINIMUM_DISTANCE_METERS = 5;

    private static final int PROXIMITY_DISTANCE_METERS = 100;

    private Context context;
    private Reaction reaction;
    private AddressReference address;
    private Location target;

    private boolean wasSatisfied;

    private LocationManager locationManager;
    private String provider;

    public PositionListener(Context context, AddressReference address, Reaction reaction) {
        this.context = context;
        this.address = address;
        this.target = LocationConverter.asLocation(address.getAddress());
        this.reaction = reaction;
    }

    @Override
    public void register() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, MINIMUM_TIME_MILLIS, MINIMUM_DISTANCE_METERS, this);
        handler.postDelayed(startRunnable, 5000);
    }

    @Override
    public void unregister() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
        handler.removeCallbacks(startRunnable);
    }

    @Override
    public String getEventID() {
        return EventsRegistry.POSITION.getEventId();
    }

    @Override
    public String getEventExtra() {
        return address.getId().toString();
    }

    @Override
    public int getEventName() {
        return EventsRegistry.POSITION.getLabel();
    }

    @Override
    public String getReactionID() {
        return reaction.getReactionID();
    }

    @Override
    public String getReactionExtra() {
        return reaction.getExtra();
    }

    @Override
    public int getReactionName() {
        return reaction.getName();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(!wasSatisfied && target.distanceTo(location)<PROXIMITY_DISTANCE_METERS){
            reaction.run(this);
            wasSatisfied = true;
        }
        if(wasSatisfied && target.distanceTo(location)>=PROXIMITY_DISTANCE_METERS) {
            wasSatisfied = false;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        unregister();
        register();
    }

    @Override
    public void onProviderEnabled(String s) {
        unregister();
        register();
    }

    @Override
    public void onProviderDisabled(String s) {
        unregister();
        register();
    }

    @Override
    public String asString() {
        return context.getString(getEventName()) + " " + context.getString(getReactionName());
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_location;
    }

    @Override
    public int getReactionIcon() {
        return reaction.getIcon();
    }

    @Override
    public Reaction getReaction() {
        return reaction;
    }

    public Address getAddress() {
        return address.getAddress();
    }
}
