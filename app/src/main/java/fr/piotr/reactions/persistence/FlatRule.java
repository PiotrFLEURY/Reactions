package fr.piotr.reactions.persistence;

import java.io.Serializable;

/**
 * Created by piotr_000 on 04/12/2016.
 *
 */

public class FlatRule implements Serializable {

    private String eventID;
    private String eventExtra;
    private String reactionID;
    private String reactionExtra;

    public FlatRule(){
        //
    }

    public FlatRule(String eventID, String eventExtra, String reactionID, String reactionExtra) {
        this.eventID = eventID;
        this.eventExtra = eventExtra;
        this.reactionID = reactionID;
        this.reactionExtra = reactionExtra;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventExtra() {
        return eventExtra;
    }

    public void setEventExtra(String eventExtra) {
        this.eventExtra = eventExtra;
    }

    public String getReactionID() {
        return reactionID;
    }

    public void setReactionID(String reactionID) {
        this.reactionID = reactionID;
    }

    public String getReactionExtra() {
        return reactionExtra;
    }

    public void setReactionExtra(String reactionExtra) {
        this.reactionExtra = reactionExtra;
    }
}
