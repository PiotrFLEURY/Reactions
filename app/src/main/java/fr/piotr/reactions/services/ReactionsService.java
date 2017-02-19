package fr.piotr.reactions.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import fr.piotr.reactions.ReactionsApplication;
import fr.piotr.reactions.Rule;
import fr.piotr.reactions.events.light.LightSensor;
import fr.piotr.reactions.managers.ReactionsManager;
import fr.piotr.reactions.registry.AppActions;
import fr.piotr.reactions.registry.PossibleEvents;
import fr.piotr.reactions.registry.PossibleReactions;

/**
 * Created by piotr_000 on 20/11/2016.
 *
 */

public class ReactionsService extends Service implements AppActions, PossibleEvents, PossibleReactions {

    public static AtomicBoolean running = new AtomicBoolean();

    IntentFilter filter = new IntentFilter(){{
        addAction(ACTION_REGISTER_LIGHT_SENSOR);
        addAction(ACTION_UNREGISTER_LIGHT_SENSOR);
    }};

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case ACTION_REGISTER_LIGHT_SENSOR:
                    registerLightSensor((LightSensor) intent.getSerializableExtra(EXTRA_SENSOR));
                    break;
                case ACTION_UNREGISTER_LIGHT_SENSOR:
                    unregisterLightSensor((LightSensor) intent.getSerializableExtra(EXTRA_SENSOR));
                    break;
                default:
                    Toast.makeText(context, "Lost action " + intent.getAction(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    public ReactionsService(){
        super();
    }

    private void activateRules(){
        ReactionsManager reactionsManager = ReactionsApplication.getReactionsManager();
        for(Rule rule: reactionsManager.getRules()){
            rule.activate();
        }
    }

    private void deactivateRules(){
        ReactionsManager reactionsManager = ReactionsApplication.getReactionsManager();
        for(Rule rule: reactionsManager.getRules()){
            rule.deactivate();
        }
    }

    private void unbindAndDie(){
        deactivateRules();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }



    private void registerLightSensor(LightSensor lightSensor) {
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(lightSensor, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void unregisterLightSensor(LightSensor lightSensor) {
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.unregisterListener(lightSensor);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        running.set(true);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
        activateRules();
    }

    @Override
    public void onDestroy() {
        running.set(false);
        unbindAndDie();
        super.onDestroy();
    }

}
