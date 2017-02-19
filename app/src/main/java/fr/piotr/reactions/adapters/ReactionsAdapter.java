package fr.piotr.reactions.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import fr.piotr.reactions.R;
import fr.piotr.reactions.registry.EventsRegistry;
import fr.piotr.reactions.registry.ReactionsRegistry;

public class ReactionsAdapter extends ArrayAdapter<ReactionsRegistry> {

    private Context context;
    private ReactionsRegistry[] values;

    public ReactionsAdapter(Context context, ReactionsRegistry[] values) {
        super(context, R.layout.spinner_item, values);
        this.context = context;
        this.values = values;
    }

    public int getCount(){
       return values.length;
    }

    public ReactionsRegistry getItem(int position){
       return values[position];
    }

    public long getItemId(int position){
       return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) LayoutInflater.from(context).inflate(R.layout.spinner_item, null, false);
        label.setTextColor(Color.BLACK);
        label.setText(context.getString(values[position].getLabel()));
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
        TextView label = (TextView) LayoutInflater.from(context).inflate(R.layout.spinner_item, null, false);
        label.setTextColor(Color.BLACK);
        label.setText(context.getString(values[position].getLabel()));

        return label;
    }
}