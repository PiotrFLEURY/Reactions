package fr.piotr.reactions.reactions;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.registry.ReactionsRegistry;
import fr.piotr.reactions.utils.NotificationUtils;

/**
 * Created by piotr_000 on 27/11/2016.
 *
 */

public class ChangeRingtoneReaction extends Reaction {

    private Uri uri;

    public ChangeRingtoneReaction(Context context, Uri uri){
        super(context);
        this.uri=uri;
    }

    @Override
    public String getReactionID() {
        return ReactionsRegistry.CHANGE_RINGTONE.getReactionsId();
    }

    @Override
    public int getName() {
        return R.string.ChangeRingtoneReactionName;
    }

    @Override
    public boolean run(Event event) {
        if (super.run(event)) {
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, uri);
            return true;
        }
        return false;
    }

    public String getRingtoneName(){
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        String title = ringtone.getTitle(context);
        return title;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_musique_list;
    }

    @Override
    public String getExtra() {
        return uri.toString();
    }
}
