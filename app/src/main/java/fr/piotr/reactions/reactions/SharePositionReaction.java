package fr.piotr.reactions.reactions;

import android.content.Context;
import android.location.Location;
import android.telephony.SmsManager;

import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.registry.ReactionsRegistry;
import fr.piotr.reactions.utils.LocationConverter;

/**
 * Created by piotr_000 on 22/01/2017.
 *
 */

public class SharePositionReaction extends Reaction {

    private final String phoneNumber;

    public SharePositionReaction(Context context, String phoneNumber) {
        super(context);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getReactionID() {
        return ReactionsRegistry.SHARE_POSITION.getReactionsId();
    }

    @Override
    public int getName() {
        return ReactionsRegistry.SHARE_POSITION.getLabel();
    }

    @Override
    public int getIcon() {
        return ReactionsRegistry.SHARE_POSITION.getIcon();
    }

    @Override
    public String getExtra() {
        return phoneNumber;
    }

    @Override
    public boolean run(Event event) {
        boolean run = super.run(event);
        if(run){
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, getPosition(), null, null);
        }
        return run;
    }

    public String getPosition() {
        Location currentLocation = LocationConverter.getCurrentLocation(context);
        String link = "http://maps.google.com?daddr=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "&amp;ll=";
        return link;
    }

}
