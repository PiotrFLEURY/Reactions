package fr.piotr.reactions.reactions;

import android.content.Context;
import android.os.Vibrator;

import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.registry.ReactionsRegistry;

/**
 * Created by piotr_000 on 30/12/2016.
 *
 *
 */

public class VibrateReaction extends Reaction {

    private long[] pattern;

    public VibrateReaction(Context context, long[] pattern) {
        super(context);
        this.pattern = pattern;
    }

    @Override
    public String getReactionID() {
        return ReactionsRegistry.VIBRATE.getReactionsId();
    }

    @Override
    public int getName() {
        return R.string.VibrateReactionName;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_vibration_white_24dp;
    }

    @Override
    public String getExtra() {
        return null;
    }

    @Override
    public boolean run(Event event) {
        boolean shouldRun = super.run(event);
        if(shouldRun){
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, -1);
        }
        return shouldRun;
    }
}
