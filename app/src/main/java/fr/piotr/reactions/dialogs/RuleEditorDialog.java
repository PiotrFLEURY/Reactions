package fr.piotr.reactions.dialogs;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import fr.piotr.reactions.MainActivity;
import fr.piotr.reactions.R;
import fr.piotr.reactions.adapters.EventsAdapter;
import fr.piotr.reactions.adapters.ReactionsAdapter;
import fr.piotr.reactions.registry.EventsRegistry;
import fr.piotr.reactions.registry.ReactionsRegistry;

/**
 * Created by piotr_000 on 10/12/2016.
 *
 */

public class RuleEditorDialog extends AlertDialog {

    private Spinner spEvent;
    private Spinner spReaction;

    public RuleEditorDialog(Context context){
        super(context);

        View dialogLayout = getLayoutInflater().inflate(R.layout.edit_rule, null);

        spEvent = (Spinner) dialogLayout.findViewById(R.id.edit_rule_sp_event_name);
        final EventsAdapter eventsAdapter = new EventsAdapter(getContext(), EventsRegistry.values());
        spEvent.setAdapter(eventsAdapter);

        spReaction = (Spinner) dialogLayout.findViewById(R.id.edit_rule_sp_reaction_name);
        final ReactionsAdapter reactionsAdapter = new ReactionsAdapter(getContext(), ReactionsRegistry.values());
        spReaction.setAdapter(reactionsAdapter);

        Button btnEnd = (Button) dialogLayout.findViewById(R.id.edit_rule_end_btn);

        setView(dialogLayout);

        btnEnd.setOnClickListener(view1 -> onValidateRule());
    }

    private void onValidateRule() {
//        Intent intent = new Intent(MainActivity.EVENT_ADD_RULE);
//        intent.putExtra(MainActivity.EXTRA_RULE_EVENT, (EventsRegistry)spEvent.getSelectedItem());
//        intent.putExtra(MainActivity.EXTRA_RULE_REACTION, (ReactionsRegistry)spReaction.getSelectedItem());
//        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        dismiss();
    }
}
