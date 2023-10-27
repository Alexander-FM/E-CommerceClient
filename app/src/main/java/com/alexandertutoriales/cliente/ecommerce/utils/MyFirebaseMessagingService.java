package com.alexandertutoriales.cliente.ecommerce.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.alexandertutoriales.cliente.ecommerce.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private PendingIntent pendingIntent;
    private static final String CHANNEL_ID = "canal";
    @Override
    public void onMessageReceived(RemoteMessage message) {
        // SI QUIERO PROCESAR DATOS DE LA APLICACIÓN
        if (message.getData().size() > 0) {
            Log.d(TAG, "Message data payload:" + message.getData());
        }
        if (message.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + message.getNotification().getBody());
            String title = message.getNotification().getTitle();
            String body = message.getNotification().getBody();

            // Aquí puedes mostrar la notificación
            showNotification(title, body);
        }
        super.onMessageReceived(message);
    }

    private void showNotification(String title, String message) {
        // Crea una notificación usando un NotificationCompat.Builder
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "New", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Obtén un NotificationManager para mostrar la notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }
}
