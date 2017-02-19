package fr.piotr.reactions.daos;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.piotr.reactions.Rule;
import fr.piotr.reactions.Rules;
import fr.piotr.reactions.persistence.FlatRule;
import fr.piotr.reactions.utils.FlatRuleConverter;

/**
 * Created by piotr_000 on 21/01/2017.
 *
 */

public class RulesDAO extends AbstractJsonDAO {

    private static final String FILE_DATA_NAME = "ReactionsRules.json";
    private static final String EVENT_ID = "EVENT_ID";
    private static final String EVENT_EXTRA = "EVENT_EXTRA";
    private static final String REACTION_ID = "REACTION_ID";
    private static final String REACTION_EXTRA = "REACTION_EXTRA";

    public Rules loadRules(Context context){
        Rules rules = new Rules();
        List<FlatRule> flatRules = loadFromFile(context);
        for (FlatRule flatRule : flatRules) {
            try {
                Rule rule = FlatRuleConverter.buildRule(context, flatRule);
                rules.add(rule);
            } catch (Exception e){
                Log.e(getClass().getSimpleName(), e.getMessage());
            }
        }
        return rules;
    }

    public void save(Context context, Rules rules) {
        try {
            JSONArray jsonRules = new JSONArray();
            for (Rule rule : rules) {
                JSONObject jsonRule = new JSONObject();
                jsonRule.put(EVENT_ID, rule.getEvent().getEventID());
                jsonRule.put(EVENT_EXTRA, rule.getEvent().getEventExtra());
                jsonRule.put(REACTION_ID, rule.getEvent().getReactionID());
                jsonRule.put(REACTION_EXTRA, rule.getEvent().getReactionExtra());

                jsonRules.put(jsonRule);
            }
            String json = jsonRules.toString();
            Log.d(getClass().getSimpleName(), json);
            saveFile(context, json, FILE_DATA_NAME);
        } catch (JSONException e){
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    private List<FlatRule> loadFromFile(Context context) {
        List<FlatRule> flatRules = new ArrayList<>();
        try {
            JSONArray jsonRules = new JSONArray(loadJsonFromFile(context, FILE_DATA_NAME));
            for(int i=0;i<jsonRules.length();i++){
                JSONObject jsonObject = jsonRules.getJSONObject(i);
                String eventID= jsonObject.getString(EVENT_ID);
                String eventExtra = null;
                if(jsonObject.has(EVENT_EXTRA)) {
                    eventExtra = jsonObject.getString(EVENT_EXTRA);
                }
                String reactionID= jsonObject.getString(REACTION_ID);
                String reactionExtra=null;
                if(jsonObject.has(REACTION_EXTRA)) {
                    reactionExtra = jsonObject.getString(REACTION_EXTRA);
                }
                flatRules.add(new FlatRule(eventID, eventExtra, reactionID, reactionExtra));
            }
        } catch(JSONException e){
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
        return flatRules;
    }

}
