package fr.piotr.reactions;

import android.app.Application;

import fr.piotr.reactions.managers.AddressManager;
import fr.piotr.reactions.managers.ReactionsManager;

/**
 * Created by piotr_000 on 04/12/2016.
 *
 */

public class ReactionsApplication extends Application {

    private static ReactionsApplication INSTANCE;

    private static ReactionsApplication getInstance(){
        return INSTANCE;
    }

    ReactionsManager reactionsManager;
    AddressManager addressManager;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        addressManager = new AddressManager(getApplicationContext());
        reactionsManager = new ReactionsManager(getApplicationContext());
        reactionsManager.loadRules();

    }

    public static ReactionsManager getReactionsManager() {
        return getInstance().reactionsManager;
    }

    public static AddressManager getAddressManager() {
        return getInstance().addressManager;
    }
}
