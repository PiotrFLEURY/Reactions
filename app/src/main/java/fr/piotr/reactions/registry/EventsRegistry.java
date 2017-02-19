package fr.piotr.reactions.registry;

import fr.piotr.reactions.R;

/**
 * Created by piotr_000 on 27/11/2016.
 *
 */

public enum EventsRegistry {

    DARKNESS(PossibleEvents.EVENT_LIGHT_LEVEL, PossibleEvents.EXTRA_LIGHT_DARKNESS, R.drawable.ic_brightness, R.string.DarknessSensorName),
    BRIGHTNESS(PossibleEvents.EVENT_LIGHT_LEVEL, PossibleEvents.EXTRA_LIGHT_BRIGHTNESS, R.drawable.ic_brightness, R.string.BrightnessSensorName),
    CHARGE(PossibleEvents.EVENT_CHARGING, null, R.drawable.ic_charge, R.string.ChargeEventName),
    POSITION(PossibleEvents.EVENT_POSITION, null, R.drawable.ic_location, R.string.PositionEventName),
    TIME(PossibleEvents.EVENT_TIME, null, R.drawable.ic_time, R.string.TimeEventName),
    SMS(PossibleEvents.EVENT_SMS, null, R.drawable.ic_sms, R.string.SmsEventName);

    String eventId;
    String extra;
    int icon;
    int label;

    EventsRegistry(String eventId, String extra, int icon, int label){
        this.eventId=eventId;
        this.extra=extra;
        this.icon=icon;
        this.label=label;
    }

    public String getEventId() {
        return eventId;
    }

    public int getLabel() {
        return label;
    }

    public int getIcon() {
        return icon;
    }

    public String getExtra() {
        return extra;
    }
}
