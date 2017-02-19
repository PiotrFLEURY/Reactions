package fr.piotr.reactions.events.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.reactions.Reaction;
import fr.piotr.reactions.registry.EventsRegistry;

/**
 * Created by piotr_000 on 22/01/2017.
 *
 */

public class SmsEvent extends BroadcastReceiver implements Event{

    public static final String EVENT_NEW_SMS = "EVENT_NEW_SMS";
    public static final String EXTRA_SENDER = "EXTRA_SENDER";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    IntentFilter filter = new IntentFilter(EVENT_NEW_SMS);

    private final Context context;
    private final String textMessage;
    private final Reaction reaction;

    public SmsEvent(Context context, String textMessage, Reaction reaction) {
        this.context = context;
        this.textMessage = textMessage;
        this.reaction = reaction;
    }

    @Override
    public void register() {
        LocalBroadcastManager.getInstance(context).registerReceiver(this, filter);
    }

    @Override
    public void unregister() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

    @Override
    public String getEventID() {
        return EventsRegistry.SMS.getEventId();
    }

    @Override
    public String getEventExtra() {
        return textMessage;
    }

    @Override
    public int getEventName() {
        return EventsRegistry.SMS.getLabel();
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
    public String asString() {
        return context.getString(getEventName()) + " " + context.getString(getReactionName());
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_sms;
    }

    @Override
    public int getReactionIcon() {
        return reaction.getIcon();
    }

    @Override
    public Reaction getReaction() {
        return reaction;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        String sender = intent.getStringExtra(EXTRA_SENDER);
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        if(message.contains(textMessage)){
            reaction.run(this);
        }
    }
}
