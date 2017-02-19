package fr.piotr.reactions.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import fr.piotr.reactions.R;
import fr.piotr.reactions.registry.EventsRegistry;

public class EventsAdapter extends ArrayAdapter<EventsRegistry> {

    private Context context;
    private EventsRegistry[] values;

    public EventsAdapter(Context context, EventsRegistry[] values) {
        super(context, android.R.layout.simple_spinner_item, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
       return values.length;
    }

    public EventsRegistry getItem(int position){
       return values[position];
    }

    public long getItemId(int position){
       return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        label.setTextColor(Color.BLACK);
        label.setCompoundDrawables(null, context.getResources().getDrawable(values[position].getIcon()), null, null);
        label.setText(context.getString(values[position].getLabel()));

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
//        CardView card = (CardView) LayoutInflater.from(context).inflate(R.layout.rule_item, parent, false);
//        card.setCardBackgroundColor(context.getColor(R.color.colorPrimaryDark));
//        TextView label = (TextView) card.findViewById(R.id.event_item_name);
//        label.setTextColor(Color.WHITE);
//        label.setText(context.getString(values[position].getLabel()));

        TextView label = (TextView) LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        label.setTextColor(Color.BLACK);
        label.setText(context.getString(values[position].getLabel()));

        return label;
    }
}