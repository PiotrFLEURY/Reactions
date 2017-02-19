package fr.piotr.reactions.events;

import fr.piotr.reactions.reactions.Reaction;

/**
 * Created by piotr_000 on 27/11/2016.
 *
 */

public interface Event {

    void register();

    void unregister();

    String getEventID();

    String getEventExtra();

    int getEventName();

    String getReactionID();

    String getReactionExtra();

    int getReactionName();

    String asString();

    int getIcon();

    int getReactionIcon();

    Reaction getReaction();
}
