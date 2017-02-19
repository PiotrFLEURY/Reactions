package fr.piotr.reactions.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Locale;

import fr.piotr.reactions.R;

/**
 * Created by piotr_000 on 04/12/2016.
 *
 */

public class LocationConverter {

//    public static String fromAddress(Address address){
//        return address.getAddressLine(0)+":"+address.getLongitude()+";"+address.getLatitude();
//    }

//    public static Address fromString(String str){
//        Address address = new Address(Locale.getDefault());
//
//        String[] parts = str.split(":");
//
//        String addressLine0 = parts[0];
//        address.setAddressLine(0, addressLine0);
//
//        String[] coordinates = parts[1].split(";");
//        address.setLongitude(Double.valueOf(coordinates[0]));
//        address.setLatitude(Double.valueOf(coordinates[1]));
//        return address;
//    }

    public static Location asLocation(Address address){
        Location location = new Location(LocationManager.NETWORK_PROVIDER);
        location.setLongitude(address.getLongitude());
        location.setLatitude(address.getLatitude());
        return location;
    }

    public static String asDisplayAddress(Context context, Address address, Location currentLocation){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            sb.append(address.getAddressLine(i)).append("\n");
        }
        if(address.getPostalCode()!=null) {
            sb.append(address.getPostalCode()).append(" ");
        }
        if(address.getLocality()!=null) {
            sb.append(address.getLocality()).append("\n");
        }
        if(address.getCountryName()!=null) {
            sb.append(address.getCountryName()).append(" ");
        }
//        sb.append(address.getLongitude()).append("\n");
//        sb.append(address.getLatitude()).append("\n");
        if(currentLocation!=null){
            sb.append(distance(context, asLocation(address), currentLocation));
        }
        return sb.toString();
    }

    private static String distance(Context context, Location a, Location b){
        float distanceEnMetres = a.distanceTo(b);
        if(distanceEnMetres>1000){
            return "~ " + ((int)distanceEnMetres/1000) + " " + context.getString(R.string.kilometres);
        }
        return "~ " + (int)distanceEnMetres + " " + context.getString(R.string.metres);
    }

    public static Location getCurrentLocation(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            try {
                return getLastKnownLocation(context);
            } catch (Exception e) {
                Log.e(LocationConverter.class.getSimpleName(), e.getMessage());//FIXME
            }
        }
        return null;
    }

    private static Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        crit.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(crit, true);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
        Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
        locationManager.removeUpdates(locationListener);
        return lastKnownLocation;
    }

}
