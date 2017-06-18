package fr.piotr.reactions.registry;

import fr.piotr.reactions.R;

/**
 * Created by piotr_000 on 27/11/2016.
 *
 */

public enum EventsRegistry {

    DARKNESS(PossibleEvents.EVENT_LIGHT_LEVEL, PossibleEvents.EXTRA_LIGHT_DARKNESS, R.drawable.ic_brightness_medium_white_24dp, R.string.DarknessSensorName),
    BRIGHTNESS(PossibleEvents.EVENT_LIGHT_LEVEL, PossibleEvents.EXTRA_LIGHT_BRIGHTNESS, R.drawable.ic_brightness_medium_white_24dp, R.string.BrightnessSensorName),
    CHARGE(PossibleEvents.EVENT_CHARGING, null, R.drawable.ic_battery_charging_full_white_24dp, R.string.ChargeEventName),
    POSITION(PossibleEvents.EVENT_POSITION, null, R.drawable.ic_location_on_white_24dp, R.string.PositionEventName),
    TIME(PossibleEvents.EVENT_TIME, null, R.drawable.ic_access_time_white_24dp, R.string.TimeEventName),
    SMS(PossibleEvents.EVENT_SMS, null, R.drawable.ic_sms_white_24dp, R.string.SmsEventName);

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
