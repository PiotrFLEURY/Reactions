package fr.piotr.reactions.events.charge;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.ReactionReceiver;
import fr.piotr.reactions.reactions.Reaction;
import fr.piotr.reactions.registry.EventsRegistry;

/**
 * Created by piotr_000 on 01/01/2017.
 *
 */

public class ChargeEvent extends ReactionReceiver {

    public ChargeEvent(Reaction reaction, Context context) {
        super(EventsRegistry.CHARGE.getEventId(), EventsRegistry.CHARGE.getLabel(), R.drawable.ic_battery_charging_full_white_24dp, reaction, new IntentFilter(Intent.ACTION_POWER_CONNECTED), context);
    }
}
