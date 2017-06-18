package fr.piotr.reactions.reactions;

import android.content.Context;
import android.os.PowerManager;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.registry.ReactionsRegistry;
import fr.piotr.reactions.utils.NotificationUtils;

/**
 * Created by piotr_000 on 20/11/2016.
 *
 */

public class WakeUpScreenReaction extends Reaction {

    public WakeUpScreenReaction(Context context){
        super(context);
    }

    @Override
    public boolean run(Event event) {
        if (super.run(event)) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "WakeUpScreenReaction");
            wakeLock.acquire();
            wakeLock.release();
            return true;
        }
        return false;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_screen_lock_portrait_white_24dp;
    }

    @Override
    public String getExtra() {
        return null;
    }

    @Override
    public String getReactionID() {
        return ReactionsRegistry.WAKEUP_SCREEN.getReactionsId();
    }

    @Override
    public int getName() {
        return R.string.WakeUpScreenReactionName;
    }

}
