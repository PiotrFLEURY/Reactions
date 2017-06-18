package fr.piotr.reactions.reactions;

import android.content.Context;
import android.media.AudioManager;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.registry.ReactionsRegistry;
import fr.piotr.reactions.utils.NotificationUtils;

/**
 * Created by piotr_000 on 20/11/2016.
 *
 */

public class ActivateRingtoneReaction extends Reaction {

    public ActivateRingtoneReaction(Context context){
        super(context);
    }

    @Override
    public boolean run(Event event) {
        if(super.run(event)) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            return true;
        }
        return false;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_music_note_white_24dp;
    }

    @Override
    public String getExtra() {
        return null;
    }

    @Override
    public String getReactionID() {
        return ReactionsRegistry.RINGTONE.getReactionsId();
    }

    @Override
    public int getName() {
        return R.string.ActivateRingtoneReactionName;
    }

}
