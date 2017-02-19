package fr.piotr.reactions.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fr.piotr.reactions.reactions.Reaction;

/**
 * Created by piotr_000 on 20/11/2016.
 *
 */

public class ReactionReceiver extends BroadcastReceiver implements Event {

    String eventID;
    int eventName;
    int eventIcon;
    Reaction reaction;
    IntentFilter filter;
    Context context;

    public ReactionReceiver(String eventID, int eventName, int eventIcon, Reaction reaction, IntentFilter filter, Context context){
        this.eventID=eventID;
        this.eventName=eventName;
        this.eventIcon=eventIcon;
        this.reaction=reaction;
        this.filter=filter;
        this.context=context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        reaction.run(this);
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
        return eventID;
    }

    @Override
    public String getEventExtra() {
        return null;
    }

    @Override
    public int getEventName() {
        return eventName;
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
    public int getIcon() {
        return eventIcon;
    }

    @Override
    public int getReactionIcon() {
        return reaction.getIcon();
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
    public Reaction getReaction() {
        return reaction;
    }
}
