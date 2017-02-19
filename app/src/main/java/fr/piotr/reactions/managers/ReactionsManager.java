package fr.piotr.reactions.managers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.List;

import fr.piotr.reactions.MainActivity;
import fr.piotr.reactions.Rule;
import fr.piotr.reactions.Rules;
import fr.piotr.reactions.daos.RulesDAO;
import fr.piotr.reactions.persistence.FlatRule;
import fr.piotr.reactions.reactions.Reaction;
import fr.piotr.reactions.utils.FlatRuleConverter;

/**
 * Created by piotr_000 on 27/11/2016.
 *
 */

public class ReactionsManager {

    Context context;
    private RulesDAO rulesDAO;

    private Rules rules = new Rules();

    private Reaction lastReaction;

    public ReactionsManager(Context context){
        this.context = context;
        rulesDAO = new RulesDAO();
    }

    public List<Rule> getRules() {
        return rules.getRules();
    }

    public void add(FlatRule flatRule){
        Rule rule = FlatRuleConverter.buildRule(context, flatRule);
        add(rule);
    }

    public void add(Rule rule) {
        rules.add(rule);
        rulesDAO.save(context, rules);
    }

    public void remove(Rule rule) {
        rule.deactivate();
        rules.remove(rule);
        rulesDAO.save(context, rules);
    }

    public void setLastReaction(Reaction lastReaction) {
        this.lastReaction = lastReaction;
    }

    public boolean isLastReaction(Reaction reaction){
        return reaction.equals(lastReaction);
    }

    public void loadRules() {
        this.rules = rulesDAO.loadRules(context);
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(MainActivity.EVENT_REFRESH));
    }
}
