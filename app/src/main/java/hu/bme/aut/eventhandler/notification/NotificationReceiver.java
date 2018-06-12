package hu.bme.aut.eventhandler.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import hu.bme.aut.eventhandler.EventHandlerActivity;
import hu.bme.aut.eventhandler.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotificationReceiver extends BroadcastReceiver {
    public static int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean show = sp.getBoolean("notification", false);

        if (!show) {
            return;
        }

        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // create notification channel (for Android O, API 26 needed)
        String id = "channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(id, "event_notification", importance);

        channel.setDescription("notification_channel");
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{1000, 1000, 1000});
        notificationManager.createNotificationChannel(channel);

        Intent i = new Intent(context, EventHandlerActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(context, id)
                .setContentTitle(name)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_calendar_clock_black_48dp)
                .setContentIntent(pIntent)
                .setChannelId(id)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(notificationId++, notification);
    }
}
