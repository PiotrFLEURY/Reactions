package fr.piotr.reactions.events.light;

import android.content.Context;

import fr.piotr.reactions.reactions.Reaction;
import fr.piotr.reactions.registry.EventsRegistry;
import fr.piotr.reactions.registry.PossibleEvents;

/**
 * Created by piotr_000 on 20/11/2016.
 *
 */

public class DarknessSensor extends LightSensor{

    private Reaction reaction;

    public DarknessSensor(Reaction reaction, Context context){
        super(context);
        this.reaction = reaction;
    }


    @Override
    protected boolean satisfied(float value) {
        return value==0;
    }

    @Override
    protected void react() {
        reaction.run(this);
    }

    @Override
    public String getEventID() {
        return EventsRegistry.BRIGHTNESS.getEventId();
    }

    @Override
    public String getEventExtra() {
        return PossibleEvents.EXTRA_LIGHT_DARKNESS;
    }

    @Override
    public int getEventName() {
        return EventsRegistry.DARKNESS.getLabel();
    }

    @Override
    public String getReactionID() {
        return reaction.getReactionID();
    }

    @Override
    public String getReactionExtra() {
        return reaction.getExtra();
    }

    @Override
    public int getReactionName() {
        return reaction.getName();
    }

    @Override
    public int getReactionIcon() {
        return reaction.getIcon();
    }

    @Override
    public Reaction getReaction() {
        return reaction;
    }
}
