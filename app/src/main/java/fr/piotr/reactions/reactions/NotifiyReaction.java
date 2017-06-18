package fr.piotr.reactions.reactions;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.registry.ReactionsRegistry;

/**
 * Created by piotr_000 on 30/12/2016.
 *
 */

public class NotifiyReaction extends Reaction {

    public NotifiyReaction(Context context) {
        super(context);
    }

    @Override
    public String getReactionID() {
        return ReactionsRegistry.NOTIFY.getReactionsId();
    }

    @Override
    public int getName() {
        return R.string.NotifyReactionName;
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
    public boolean run(Event event) {
        boolean run = super.run(event);
        if(run){
            try {
                playNotification();
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), e.getMessage());
            }
        }
        return run;
    }

    private void playNotification() throws IOException {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }
}
