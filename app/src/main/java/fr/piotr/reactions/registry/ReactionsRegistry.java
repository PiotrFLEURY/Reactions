package fr.piotr.reactions.registry;

import fr.piotr.reactions.R;

/**
 * Created by piotr_000 on 27/11/2016.
 *
 */

public enum ReactionsRegistry {

//    TOAST(PossibleReactions.MAKE_TOAST, R.string.MakeToastReactionnName),
    VIBRATOR(PossibleReactions.ACTIVATE_VIBRATOR, R.drawable.ic_vibration_white_24dp, R.string.ActivateVibratorReactionName),
    RINGTONE(PossibleReactions.ACTIVATE_RINGTONE, R.drawable.ic_music_note_white_24dp, R.string.ActivateRingtoneReactionName),
    WAKEUP_SCREEN(PossibleReactions.WAKEUP_SCREEN, R.drawable.ic_screen_lock_portrait_white_24dp, R.string.WakeUpScreenReactionName),
    CHANGE_RINGTONE(PossibleReactions.CHANGE_RINGTONE, R.drawable.ic_queue_music_white_24dp, R.string.ChangeRingtoneReactionName),
//    GO_AIRPLANE_MODE(PossibleReactions.GO_AIRPLANE_MODE, R.string.GoAirplaneModeReactionName)
    SET_WALLPAPER(PossibleReactions.SET_WALLPAPER, R.drawable.ic_wallpaper_white_24dp, R.string.SetWallpaperReactionName),
    VIBRATE(PossibleReactions.VRIBRATE, R.drawable.ic_vibration_white_24dp, R.string.VibrateReactionName),
    NOTIFY(PossibleReactions.NOTIFY, R.drawable.ic_music_note_white_24dp, R.string.NotifyReactionName),
    EMAIL(PossibleReactions.EMAIL, R.drawable.ic_email_white_24dp, R.string.SendMailReactionName),
    SHARE_POSITION(PossibleReactions.SHARE_POSITION, R.drawable.ic_location_on_white_24dp, R.string.SharePositionReactionName)
    ;

    String reactionsId;
    private int icon;
    public int label;

    ReactionsRegistry(String reactionsId, int icon, int label){
        this.reactionsId =reactionsId;
        this.icon=icon;
        this.label=label;
    }

    public String getReactionsId() {
        return reactionsId;
    }

    public int getLabel(){
        return label;
    }

    public int getIcon() {
        return icon;
    }
}
