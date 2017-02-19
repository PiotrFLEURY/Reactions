package fr.piotr.reactions.builder;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.net.Uri;

import java.util.EnumSet;

import fr.piotr.reactions.events.time.HourMinute;
import fr.piotr.reactions.persistence.AddressReference;
import fr.piotr.reactions.registry.EventsRegistry;
import fr.piotr.reactions.registry.ReactionsRegistry;

/**
 * Created by piotr_000 on 10/12/2016.
 *
 */

public class RuleBuilder {

    Context context;
    EventsRegistry eventsRegistry;
    ReactionsRegistry reactionsRegistry;

    private Uri pickedRingtone;
    private Integer pickedImageId;
    private AddressReference pickedAddress;
    private long[] pickedPattern;
    private HourMinute pickedHourMinute;
    private String pickedMailMessage;
    private String pickedMessageText;
    private String pickedPhoneNumber;

    public RuleBuilder(Context context) {
        this.context=context;
    }

    public boolean isEventValid(){
        if(eventsRegistry==null){
            return false;
        }
        if(EventsRegistry.POSITION.equals(eventsRegistry)
                && pickedAddress==null){
            return false;
        }
        if(EventsRegistry.TIME.equals(eventsRegistry)
                && pickedHourMinute==null){
            return false;
        }
        if(EventsRegistry.SMS.equals(eventsRegistry)
                && pickedMessageText==null){
            return false;
        }
        return true;
    }

    public boolean isReactionValid(){
        if(reactionsRegistry==null){
            return false;
        }
        if(ReactionsRegistry.CHANGE_RINGTONE.equals(reactionsRegistry)
                && pickedRingtone==null){
            return false;
        }
        if(ReactionsRegistry.SET_WALLPAPER.equals(reactionsRegistry)
                && pickedImageId==null){
            return false;
        }
        if(ReactionsRegistry.VIBRATE.equals(reactionsRegistry)
                && pickedPattern==null){
            return false;
        }
        return true;
    }

    public EventsRegistry getEventsRegistry() {
        return eventsRegistry;
    }

    public void setEventsRegistry(EventsRegistry eventsRegistry) {
        this.eventsRegistry = eventsRegistry;
    }

    public ReactionsRegistry getReactionsRegistry() {
        return reactionsRegistry;
    }

    public void setReactionsRegistry(ReactionsRegistry reactionsRegistry) {
        this.reactionsRegistry = reactionsRegistry;
    }

    public Uri getPickedRingtone() {
        return pickedRingtone;
    }

    public void setPickedRingtone(Uri pickedRingtone) {
        this.pickedRingtone = pickedRingtone;
    }

    public Integer getPickedImageId() {
        return pickedImageId;
    }

    public void setPickedImageId(Integer pickedImageId) {
        this.pickedImageId = pickedImageId;
    }

    public AddressReference getPickedAddress() {
        return pickedAddress;
    }

    public void setPickedAddress(AddressReference pickedAddress) {
        this.pickedAddress = pickedAddress;
    }

    public long[] getPickedPattern() {
        return pickedPattern;
    }

    public void setPickedPattern(long[] pickedPattern) {
        this.pickedPattern = pickedPattern;
    }

    public HourMinute getPickedHourMinute() {
        return pickedHourMinute;
    }

    public void setPickedHourMinute(HourMinute pickedHourMinute) {
        this.pickedHourMinute = pickedHourMinute;
    }

    public void setPickedMailMessage(String pickedMailMessage) {
        this.pickedMailMessage = pickedMailMessage;
    }

    public String getPickedMailMessage() {
        return pickedMailMessage;
    }

    public void setPickedMessageText(String pickedMessageText) {
        this.pickedMessageText = pickedMessageText;
    }

    public String getPickedMessageText() {
        return pickedMessageText;
    }

    public void setPickedPhoneNumber(String pickedPhoneNumber) {
        this.pickedPhoneNumber = pickedPhoneNumber;
    }

    public String getPickedPhoneNumber() {
        return pickedPhoneNumber;
    }
}
