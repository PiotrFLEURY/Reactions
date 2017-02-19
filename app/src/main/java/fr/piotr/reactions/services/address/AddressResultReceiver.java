package fr.piotr.reactions.services.address;

import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.Toast;

import fr.piotr.reactions.services.GeocodeAddressIntentService;

public abstract class AddressResultReceiver extends ResultReceiver {

    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, final Bundle resultData) {
        if (resultCode == GeocodeAddressIntentService.Constants.SUCCESS_RESULT) {
            final Address address = resultData.getParcelable(GeocodeAddressIntentService.Constants.RESULT_ADDRESS);
            onAddressResolved(address);
        } else {
            // TODO
        }
    }

    public abstract void onAddressResolved(Address address);
}