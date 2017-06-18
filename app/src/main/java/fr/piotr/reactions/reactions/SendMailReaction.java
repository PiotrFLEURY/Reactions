package fr.piotr.reactions.reactions;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import fr.piotr.reactions.R;
import fr.piotr.reactions.email.SendMail;
import fr.piotr.reactions.events.Event;
import fr.piotr.reactions.registry.ReactionsRegistry;

/**
 * Created by piotr_000 on 08/01/2017.
 *
 */

public class SendMailReaction extends Reaction {

    String email;
    String message;

    public SendMailReaction(Context context, String message) {
        super(context);
        this.email = getEmail();
        this.message=message;
    }

    @Override
    public String getReactionID() {
        return ReactionsRegistry.EMAIL.getReactionsId();
    }

    @Override
    public int getName() {
        return R.string.SendMailReactionName;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_email_white_24dp;
    }

    @Override
    public String getExtra() {
        return message;
    }

    @Override
    public boolean run(Event event) {
        boolean run = super.run(event);
        if(run){
            new SendMail(context, email, message).execute();
        }
        return run;
    }

    private String getEmail() {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }
}
