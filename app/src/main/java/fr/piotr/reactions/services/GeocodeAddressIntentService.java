package fr.piotr.reactions.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by piotr_000 on 26/12/2016.
 *
 */

public class GeocodeAddressIntentService extends IntentService {

    public static interface Constants {
        String RECEIVER = "RECEIVER";
        String LOCATION_NAME_DATA_EXTRA = "LOCATION_NAME_DATA_EXTRA";

        int FAILURE_RESULT = -1;
        int SUCCESS_RESULT = 1;

        String RESULT_ADDRESS = "RESULT_ADDRESS";
        String RESULT_DATA_KEY = "RESULT_DATA_KEY";
    }

    protected ResultReceiver resultReceiver;
    private static final String TAG = "FetchAddyIntentService";

    public GeocodeAddressIntentService() {
        super("GeocodeAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        String name = intent.getStringExtra(Constants.LOCATION_NAME_DATA_EXTRA);

        List<Address> addresses = getAddresses(geocoder, name);

        if (addresses == null || addresses.size()  == 0) {
            deliverResultToReceiver(Constants.FAILURE_RESULT, null, null);
        } else {
            Address address = addresses.get(0);
            List<String> addressFragments = new ArrayList<>();

            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            String addressName = TextUtils.join(System.getProperty("line.separator"), addressFragments);
            deliverResultToReceiver(Constants.SUCCESS_RESULT, addressName, address);
        }
    }

    @Nullable
    private List<Address> getAddresses(Geocoder geocoder, String name) {
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(name, 1);
        } catch (IOException e) {
            Log.e(TAG, "Service not available", e);
        }
        return addresses;
    }

    private void deliverResultToReceiver(int resultCode, String message, Address address) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_ADDRESS, address);
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        resultReceiver.send(resultCode, bundle);
    }
}
