package fr.piotr.reactions.adapters;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import fr.piotr.reactions.R;
import fr.piotr.reactions.ReactionsApplication;
import fr.piotr.reactions.Rule;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.events.position.PositionListener;
import fr.piotr.reactions.events.time.TimeEvent;
import fr.piotr.reactions.reactions.ChangeRingtoneReaction;
import fr.piotr.reactions.reactions.Reaction;
import fr.piotr.reactions.reactions.SetWallpaperReaction;
import fr.piotr.reactions.registry.EventsRegistry;
import fr.piotr.reactions.registry.PossibleEvents;
import fr.piotr.reactions.registry.ReactionsRegistry;
import fr.piotr.reactions.utils.LocationConverter;

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.RuleViewHolder> {

    private Context context;
    private List<Rule> mDataset = ReactionsApplication.getReactionsManager().getRules();
    private Location currentLocation;

    public static class RuleViewHolder extends RecyclerView.ViewHolder {
        public CardView layout;
        public RuleViewHolder(CardView v) {
            super(v);
            layout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RulesAdapter(Context context, Location currentLocation) {
        this.context=context;
        this.currentLocation=currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public RuleViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rule, parent, false);
        return new RuleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RuleViewHolder holder, int position) {

        Rule rule = mDataset.get(position);
        holder.layout.setOnClickListener(view -> {
            onItemClick(rule);
        });

        ImageView ivRuleEventIcon = (ImageView) holder.layout.findViewById(R.id.rule_event_icon);
        TextView tvRuleEventName = (TextView) holder.layout.findViewById(R.id.rule_event_name);
        TextView tvRuleEventParam = (TextView) holder.layout.findViewById(R.id.rule_event_parameter);
        ImageView ivReactionImage = (ImageView) holder.layout.findViewById(R.id.rule_reaction_image);
        ImageView ivRuleReactionIcon = (ImageView) holder.layout.findViewById(R.id.rule_reaction_icon);
        TextView tvRuleReactionName = (TextView) holder.layout.findViewById(R.id.rule_reaction_name);
        TextView tvReactionExtra = (TextView) holder.layout.findViewById(R.id.tv_reaction_extra);

        ivRuleEventIcon.setImageResource(rule.getEventIcon());

        tvRuleEventParam.setVisibility(View.GONE);
        if(rule.getEvent().getEventID().equals(EventsRegistry.POSITION.getEventId())
            || rule.getEvent().getEventID().equals(EventsRegistry.TIME.getEventId())
                || rule.getEvent().getEventID().equals(EventsRegistry.SMS.getEventId())) {
            tvRuleEventParam.setVisibility(View.VISIBLE);
            tvRuleEventParam.setText(getEventExtra(rule));
        }
        tvRuleEventName.setText(context.getString(rule.getEventName()));

        Reaction reaction = rule.getReaction();
        if(reaction.getReactionID().equals(ReactionsRegistry.SET_WALLPAPER.getReactionsId())){
            ivReactionImage.setVisibility(View.VISIBLE);
            ivReactionImage.setImageBitmap(((SetWallpaperReaction)reaction).getBitmap());
        } else {
            ivReactionImage.setVisibility(View.GONE);
        }
        ivRuleReactionIcon.setImageResource(rule.getReactionIcon());
        if(reaction.getReactionID().equals(ReactionsRegistry.CHANGE_RINGTONE.getReactionsId())){
            ChangeRingtoneReaction changeRingtoneReaction = (ChangeRingtoneReaction) reaction;
            tvRuleReactionName.setText(context.getString(rule.getReactionName()) + "(" + changeRingtoneReaction.getRingtoneName() + ")");
        } else {
            tvRuleReactionName.setText(context.getString(rule.getReactionName()));
        }

        tvReactionExtra.setVisibility(View.GONE);
        if(reaction.getReactionID().equals(ReactionsRegistry.SHARE_POSITION.getReactionsId())) {
            tvReactionExtra.setVisibility(View.VISIBLE);
            tvReactionExtra.setText(rule.getReaction().getExtra());
        }

    }

    private String getEventExtra(Rule rule){
        Event event = rule.getEvent();
        if(event instanceof PositionListener){
            PositionListener positionListener = (PositionListener) event;
            return LocationConverter.asDisplayAddress(context, positionListener.getAddress(), currentLocation);
        } else if(event instanceof TimeEvent){
            TimeEvent timeEvent = (TimeEvent) event;
            return timeEvent.getHourMinute().toString();
        }
        return event.getEventExtra();
    }

    private void onItemClick(Rule rule) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.confirm_delete_rule_title)
                .setMessage(R.string.confirm_delete_rule)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    ReactionsApplication.getReactionsManager().remove(rule);
                    mDataset = ReactionsApplication.getReactionsManager().getRules();
                    notifyDataSetChanged();
                })
        .setNegativeButton(R.string.no, (dialogInterface, i) -> {
            //
        }).show();

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}