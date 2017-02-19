package fr.piotr.reactions.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.piotr.reactions.MainActivity;
import fr.piotr.reactions.R;
import fr.piotr.reactions.events.Event;

/**
 * Created by piotr_000 on 27/11/2016.
 *
 */

public class NotificationUtils {

    private static final int SUMMARY_ID =1;
    private static int notificationId =2;
    private static Map<Integer, Set<Integer>> firedEvents = new HashMap<>();

    private static void addEvent(Event event){
        Set<Integer> reactionsIds = firedEvents.get(event.getEventName());
        if(reactionsIds==null){
            reactionsIds = new HashSet<>();
            firedEvents.put(event.getEventName(), reactionsIds);
        }
        reactionsIds.add(event.getReactionName());
    }

    public static void notify(Context context, int title, Event event){
        addEvent(event);
        String text = event.asString();
        String titleText = context.getString(title);
        String fullText = titleText + "\n" + text;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_small_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(titleText)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(fullText));
        PendingIntent resultPendingIntent = getPendingIntent(context);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setGroup(context.getString(event.getEventName()));
// mId allows you to update the notification later on.
        mNotificationManager.notify(notificationId++, mBuilder.build());
        notifySummary(context, titleText, mNotificationManager, event);


    }

    private static PendingIntent getPendingIntent(Context context) {
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        return stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static void notifySummary(Context context, String titleText, NotificationManager mNotificationManager, Event event) {
        //Summary
        Bitmap background = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher);

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setBackground(background);

        // Create an InboxStyle notification
        String eventName = context.getString(event.getEventName());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .setBigContentTitle(eventName)
                .setSummaryText(titleText);
        for(Integer recationNameId:firedEvents.get(event.getEventName())){
            inboxStyle.addLine(context.getString(recationNameId));
        }
        Notification summaryNotificationWithBackground =
                new NotificationCompat.Builder(context)
                .extend(wearableExtender)
                .setContentTitle(titleText)
                .setContentIntent(getPendingIntent(context))
                .setSmallIcon(R.mipmap.ic_small_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setStyle(inboxStyle)
                .setGroup(eventName)
                .setGroupSummary(true)
                .build();

        mNotificationManager.notify(SUMMARY_ID, summaryNotificationWithBackground);
    }
}
