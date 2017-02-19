package fr.piotr.reactions.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.util.UUID;

import fr.piotr.reactions.ReactionsApplication;
import fr.piotr.reactions.Rule;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.events.ReactionReceiver;
import fr.piotr.reactions.events.charge.ChargeEvent;
import fr.piotr.reactions.events.light.BrightnessSensor;
import fr.piotr.reactions.events.light.DarknessSensor;
import fr.piotr.reactions.events.light.LightSensor;
import fr.piotr.reactions.events.position.PositionListener;
import fr.piotr.reactions.events.sms.SmsEvent;
import fr.piotr.reactions.events.time.HourMinute;
import fr.piotr.reactions.events.time.TimeEvent;
import fr.piotr.reactions.persistence.AddressReference;
import fr.piotr.reactions.persistence.FlatRule;
import fr.piotr.reactions.reactions.ActivateRingtoneReaction;
import fr.piotr.reactions.reactions.ActivateVibratorReaction;
import fr.piotr.reactions.reactions.ChangeRingtoneReaction;
import fr.piotr.reactions.reactions.NotifiyReaction;
import fr.piotr.reactions.reactions.Reaction;
import fr.piotr.reactions.reactions.SendMailReaction;
import fr.piotr.reactions.reactions.SetWallpaperReaction;
import fr.piotr.reactions.reactions.SharePositionReaction;
import fr.piotr.reactions.reactions.VibrateReaction;
import fr.piotr.reactions.reactions.WakeUpScreenReaction;

import static fr.piotr.reactions.registry.PossibleEvents.EVENT_CHARGING;
import static fr.piotr.reactions.registry.PossibleEvents.EVENT_LIGHT_LEVEL;
import static fr.piotr.reactions.registry.PossibleEvents.EVENT_POSITION;
import static fr.piotr.reactions.registry.PossibleEvents.EVENT_SMS;
import static fr.piotr.reactions.registry.PossibleEvents.EVENT_TIME;
import static fr.piotr.reactions.registry.PossibleEvents.EXTRA_LIGHT_BRIGHTNESS;
import static fr.piotr.reactions.registry.PossibleEvents.EXTRA_LIGHT_DARKNESS;
import static fr.piotr.reactions.registry.PossibleReactions.ACTIVATE_RINGTONE;
import static fr.piotr.reactions.registry.PossibleReactions.ACTIVATE_VIBRATOR;
import static fr.piotr.reactions.registry.PossibleReactions.CHANGE_RINGTONE;
import static fr.piotr.reactions.registry.PossibleReactions.EMAIL;
import static fr.piotr.reactions.registry.PossibleReactions.NOTIFY;
import static fr.piotr.reactions.registry.PossibleReactions.SET_WALLPAPER;
import static fr.piotr.reactions.registry.PossibleReactions.SHARE_POSITION;
import static fr.piotr.reactions.registry.PossibleReactions.VRIBRATE;
import static fr.piotr.reactions.registry.PossibleReactions.WAKEUP_SCREEN;

/**
 * Created by piotr_000 on 04/12/2016.
 *
 */

public class FlatRuleConverter {

    public static Rule buildRule(Context context, FlatRule flatRule){
        Event event = null;
        String eventId = flatRule.getEventID();
        switch (eventId){
            case EVENT_CHARGING:
                event = buildChargeListener(context, flatRule);
                break;
            case EVENT_LIGHT_LEVEL:
                event = buildLightSensor(context, flatRule);
                break;
            case EVENT_POSITION:
                event = buildLocationListener(context, flatRule);
                break;
            case EVENT_TIME:
                event = buildTimeEvent(context, flatRule);
                break;
            case EVENT_SMS:
                event = buildSmsEvent(context, flatRule);
                break;
            default:
                Toast.makeText(context, "Unable to create event "+eventId, Toast.LENGTH_LONG).show();
                break;
        }
        return new Rule(event);
    }

    private static Event buildSmsEvent(Context context, FlatRule flatRule) {
        return new SmsEvent(context,flatRule.getEventExtra(), getReaction(context, flatRule));
    }

    private static Event buildTimeEvent(Context context, FlatRule flatRule) {
        return new TimeEvent(context, HourMinute.valueOf(flatRule.getEventExtra()), getReaction(context, flatRule));
    }

    private static PositionListener buildLocationListener(Context context, FlatRule flatRule) {
        return new PositionListener(context, getAddress(flatRule.getEventExtra()), getReaction(context, flatRule));
    }

    private static AddressReference getAddress(String eventExtra) {
        UUID uuid = UUID.fromString(eventExtra);
        return ReactionsApplication.getAddressManager().get(uuid);
    }

    private static LightSensor buildLightSensor(Context context, FlatRule flatRule) {
        LightSensor lightSensor=null;
        Reaction reaction = getReaction(context, flatRule);
        switch(flatRule.getEventExtra()){
            case EXTRA_LIGHT_BRIGHTNESS:
                lightSensor= new BrightnessSensor(reaction, context);
                break;
            case EXTRA_LIGHT_DARKNESS:
                lightSensor = new DarknessSensor(reaction, context);
                break;
        }
        return lightSensor;
    }

    private static ReactionReceiver buildChargeListener(Context context, FlatRule flatRule) {
        return new ChargeEvent(getReaction(context, flatRule), context);
    }

    private static Reaction getReaction(Context context, FlatRule flatRule){
        String reactionId = flatRule.getReactionID();
        switch (reactionId){
            case ACTIVATE_VIBRATOR:
                return new ActivateVibratorReaction(context);
            case ACTIVATE_RINGTONE:
                return new ActivateRingtoneReaction(context);
            case WAKEUP_SCREEN:
                return new WakeUpScreenReaction(context);
            case CHANGE_RINGTONE:
                return new ChangeRingtoneReaction(context, Uri.parse(flatRule.getReactionExtra()));
            case SET_WALLPAPER:
                return new SetWallpaperReaction(context, Integer.valueOf(flatRule.getReactionExtra()));
            case VRIBRATE:
                return new VibrateReaction(context, PatternConverter.asPattern(flatRule.getReactionExtra()));
            case NOTIFY:
                return new NotifiyReaction(context);
            case EMAIL:
                return new SendMailReaction(context, flatRule.getReactionExtra());
            case SHARE_POSITION:
                return new SharePositionReaction(context, flatRule.getReactionExtra());
            default:
                Toast.makeText(context, "Unable to create reaction "+reactionId, Toast.LENGTH_LONG).show();
                break;
        }
        return null;
    }
}
