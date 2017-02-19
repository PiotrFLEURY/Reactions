package fr.piotr.reactions.dialogs;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import fr.piotr.reactions.AddressListActivity;
import fr.piotr.reactions.EditRuleActivity;
import fr.piotr.reactions.R;
import fr.piotr.reactions.services.GeocodeAddressIntentService;
import fr.piotr.reactions.services.address.AddressResultReceiver;
import fr.piotr.reactions.utils.LocationConverter;

/**
 * Created by piotr_000 on 31/12/2016.
 *
 */

public class LocationPicker extends AlertDialog {

    Handler mHandler = new Handler();

    View mRootView;
    EditText etText;
    ProgressBar progress;
    TextView tvResult;
    ImageButton button;

    Address address;

    public LocationPicker(@NonNull Context context) {
        super(context);

        mRootView = LayoutInflater.from(context).inflate(R.layout.location_picker, null, false);

        etText = (EditText) mRootView.findViewById(R.id.location_picker_text);
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvResult.setText("");
                setButtonSearchMode();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        progress = (ProgressBar) mRootView.findViewById(R.id.location_picker_progress);
        progress.setVisibility(View.INVISIBLE);

        tvResult = (TextView) mRootView.findViewById(R.id.location_picker_result);

        button = (ImageButton) mRootView.findViewById(R.id.location_picker_btn);
        setButtonSearchMode();

        setView(mRootView);
    }

    private void setButtonSearchMode() {
        button.setImageResource(R.drawable.ic_search);
        button.setColorFilter(R.color.green500);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                search();
            }
        });
    }

    private void search() {
        String typedAddress = etText.getText().toString();
        pickupLocationFromAddress(typedAddress);
    }

    private void pickupLocationFromAddress(String addressName) {
        Intent intent = new Intent(getContext(), GeocodeAddressIntentService.class);
        intent.putExtra(GeocodeAddressIntentService.Constants.RECEIVER, new AddressResultReceiver(mHandler) {
            @Override
            public void onAddressResolved(Address address) {
                setAddress(address);
                progress.setVisibility(View.INVISIBLE);
                tvResult.setText(LocationConverter.asDisplayAddress(getContext(), address, null));
                setButtonValidateMode();
            }
        });
        if(addressName==null || addressName.isEmpty()) {
            setButtonSearchMode();
            return;
        }
        intent.putExtra(GeocodeAddressIntentService.Constants.LOCATION_NAME_DATA_EXTRA, addressName);
        Log.e(getClass().getSimpleName(), "Starting Service");
        getContext().startService(intent);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private void openGMaps(Address address) {
        Uri gmmIntentUri = Uri.parse("geo:"+address.getLatitude()+","+address.getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(mapIntent);
        }
    }

    private void setButtonValidateMode() {
        button.setImageResource(R.drawable.ic_accept);
        button.setColorFilter(R.color.lightblue800);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressListActivity.EVENT_LOCATION_PICKED_UP);
                intent.putExtra(AddressListActivity.EXTRA_LOCATION_ADDRESS, address);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                dismiss();
            }
        });
    }
}
