package fr.piotr.reactions.events.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import fr.piotr.reactions.services.ReactionsService;

/**
 * Created by piotr_000 on 22/01/2017.
 *
 */

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(!ReactionsService.running.get()){
            return;
        }

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (Object pdu : pdusObj) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + phoneNumber + "; message: " + message);

                    Intent intentNewSms = new Intent(SmsEvent.EVENT_NEW_SMS);
                    intentNewSms.putExtra(SmsEvent.EXTRA_SENDER, phoneNumber);
                    intentNewSms.putExtra(SmsEvent.EXTRA_MESSAGE, message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intentNewSms);

                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

}
