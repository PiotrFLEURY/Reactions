package fr.piotr.reactions;

import java.util.UUID;

import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.persistence.FlatRule;
import fr.piotr.reactions.reactions.Reaction;

/**
 * Created by piotr_000 on 27/11/2016.
 *
 */

public class Rule {

    private UUID id;
    private Event event;

    public Rule(Event event){
        this.id = UUID.randomUUID();
        this.event=event;
    }

    public UUID getId() {
        return id;
    }

    public void activate(){
        event.register();
    }

    public void deactivate(){
        event.unregister();
    }

    public int getEventIcon() {
        return event.getIcon();
    }

    public int getEventName(){
        return event.getEventName();
    }

    public int getReactionIcon() {
        return event.getReactionIcon();
    }

    public int getReactionName(){
        return event.getReactionName();
    }

    public Reaction getReaction() { return event.getReaction(); }

    public Event getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Rule && id.equals(((Rule) obj).getId());
    }

    public FlatRule toFlat() {
        return new FlatRule(event.getEventID(), event.getEventExtra(), event.getReactionID(), event.getReactionExtra());
    }

}
