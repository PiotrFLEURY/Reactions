package fr.piotr.reactions.adapters;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.piotr.reactions.AddressListActivity;
import fr.piotr.reactions.R;
import fr.piotr.reactions.ReactionsApplication;
import fr.piotr.reactions.persistence.AddressReference;
import fr.piotr.reactions.utils.LocationConverter;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private Context context;
    private List<AddressReference> mDataset = ReactionsApplication.getAddressManager().getAddressReferences();
    private Location currentLocation;
    private boolean selectionMode;

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        public View layout;
        public AddressViewHolder(View v) {
            super(v);
            layout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AddressAdapter(Context context, Location currentLocation) {
        this.context=context;
        this.currentLocation=currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address, parent, false);
        return new AddressViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {

        final AddressReference addressReference = mDataset.get(position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(addressReference);
            }

        });

        TextView tvAddress = (TextView) holder.layout.findViewById(R.id.tv_address);

        tvAddress.setText(LocationConverter.asDisplayAddress(context, addressReference.getAddress(), currentLocation));
    }

    private void onItemClick(final AddressReference addressReference) {
        if(selectionMode){
            Intent intent = new Intent(AddressListActivity.EVENT_LOCATION_SELECTED);
            intent.putExtra(AddressListActivity.EXTRA_LOCATION_UUID, addressReference.getId().toString());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } else {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.confirm_delete_address_title)
                    .setMessage(R.string.confirm_delete_address)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ReactionsApplication.getAddressManager().remove(addressReference);
                            refresh();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    }).show();
        }
    }

    public void refresh() {
        mDataset = ReactionsApplication.getAddressManager().getAddressReferences();
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
    }
}