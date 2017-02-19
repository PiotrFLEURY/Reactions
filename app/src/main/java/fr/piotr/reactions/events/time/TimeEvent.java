package fr.piotr.reactions.events.time;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Calendar;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.reactions.Reaction;
import fr.piotr.reactions.registry.EventsRegistry;

/**
 * Created by piotr_000 on 01/01/2017.
 *
 */

public class TimeEvent implements Event {

    static final String EVENT_TIME_RECEIVED = "EVENT_TIME_RECEIVED";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getReaction().run(TimeEvent.this);
        }
    };

    private IntentFilter filter = new IntentFilter(EVENT_TIME_RECEIVED);

    private Context context;
    private HourMinute hourMinute;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private Reaction reaction;

    public TimeEvent(Context context, HourMinute hourMinute, Reaction reaction){
        this.context = context;
        this.hourMinute = hourMinute;
        this.reaction = reaction;
    }


    @Override
    public void register() {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourMinute.getHour());
        calendar.set(Calendar.MINUTE, hourMinute.getMinute());

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    @Override
    public void unregister() {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);

    }

    @Override
    public String getEventID() {
        return EventsRegistry.TIME.getEventId();
    }

    @Override
    public String getEventExtra() {
        return hourMinute.toString();
    }

    @Override
    public int getEventName() {
        return R.string.TimeEventName;
    }

    @Override
    public String getReactionID() {
        return getReaction().getReactionID();
    }

    @Override
    public String getReactionExtra() {
        return getReaction().getExtra();
    }

    @Override
    public int getReactionName() {
        return getReaction().getName();
    }

    @Override
    public String asString() {
        return context.getString(getEventName()) + " " + context.getString(getReactionName());
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_time;
    }

    @Override
    public int getReactionIcon() {
        return getReaction().getIcon();
    }

    @Override
    public Reaction getReaction() {
        return reaction;
    }

    public HourMinute getHourMinute() {
        return hourMinute;
    }
}
