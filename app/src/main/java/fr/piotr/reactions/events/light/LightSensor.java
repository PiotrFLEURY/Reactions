package fr.piotr.reactions.events.light;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;

import fr.piotr.reactions.R;
import fr.piotr.reactions.registry.AppActions;
import fr.piotr.reactions.events.Event;

/**
 * Created by piotr_000 on 20/11/2016.
 *
 */

public abstract class LightSensor implements SensorEventListener, Event, Serializable {

    private float lastValue = -1;
    private Handler handler = new Handler();
    private Context context;

    boolean wasSatisfied;

    LightSensor(Context context){
        this.context=context;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float value = sensorEvent.values[0];
        if(valueChanged(value)){
            onValueChanged(value);
        }
        lastValue = value;
    }

    private boolean valueChanged(float value){
        return lastValue!=-1 && lastValue!=value;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //
    }

    private void onValueChanged(float value) {
        if(!wasSatisfied && satisfied(value)) {
            unregister();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(satisfied(lastValue)){
                        react();
                        wasSatisfied =true;
                    } else {
                        wasSatisfied =false;
                    }
                    register();
                }
            }, getDelay());
        }
        if(wasSatisfied && !satisfied(value)) {
            wasSatisfied = false;
        }
    }

    private int getDelay() {
        return context.getResources().getInteger(R.integer.lightSensorDelay);
    }

    protected abstract void react();

    protected abstract boolean satisfied(float value);


    @Override
    public void register() {
        Intent intent = new Intent(AppActions.ACTION_REGISTER_LIGHT_SENSOR);
        intent.putExtra(AppActions.EXTRA_SENSOR, this);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public void unregister() {
        Intent intent = new Intent(AppActions.ACTION_UNREGISTER_LIGHT_SENSOR);
        intent.putExtra(AppActions.EXTRA_SENSOR, this);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public String asString() {
        return context.getString(getEventName()) + " " + context.getString(getReactionName());
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_brightness_medium_white_24dp;
    }
}
