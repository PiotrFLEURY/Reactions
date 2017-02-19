package fr.piotr.reactions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.piotr.reactions.persistence.FlatRule;

/**
 * Created by piotr_000 on 04/12/2016.
 *
 */

public class Rules implements Iterable<Rule> {

    private List<Rule> rules = new ArrayList<>();

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void clear() {
        this.rules.clear();
    }

    public void add(Rule rule) {
        this.rules.add(rule);
    }

    public void remove(Rule rule) {
        this.rules.remove(rule);
    }

    private List<FlatRule> flat(){
        List<FlatRule> flatRules = new ArrayList<>();
        for (Rule rule : rules) {
            flatRules.add(rule.toFlat());
        }
        return flatRules;
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }
}
