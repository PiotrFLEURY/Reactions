package fr.piotr.reactions.reactions;

import android.content.Context;

import java.util.UUID;

import fr.piotr.reactions.R;
import fr.piotr.reactions.ReactionsApplication;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.managers.ReactionsManager;
import fr.piotr.reactions.utils.NotificationUtils;

/**
 * Created by piotr_000 on 20/11/2016.
 *
 */

public abstract class Reaction {

    private UUID id;
    protected Context context;

    public Reaction(Context context){
        this.context=context;
        this.id=UUID.randomUUID();
    }

    public abstract String getReactionID();

    public abstract int getName();

    public boolean run(Event event){
        ReactionsManager reactionsManager = ReactionsApplication.getReactionsManager();
        if(reactionsManager.isLastReaction(this)) {
            return false;
        }
        NotificationUtils.notify(context, R.string.notification_rule_fired, event);
        reactionsManager.setLastReaction(this);
        return true;
    }

    public abstract int getIcon();

    public abstract  String getExtra();

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Reaction && ((Reaction) obj).id.equals(id);
    }
}
