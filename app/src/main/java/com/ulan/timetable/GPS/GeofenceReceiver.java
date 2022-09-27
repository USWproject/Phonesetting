package com.ulan.timetable.GPS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.ulan.timetable.R;

public class GeofenceReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent.hasError()) {
                //Not proper -> error message (Toast) 출력
                String errorMessage = getErrorString(geofencingEvent.getErrorCode());
                Log.e(TAG, errorMessage);
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                return;
            }
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            // Geofence area 입장 시 무음 모드로 전환
            int geofenceTransition = geofencingEvent.getGeofenceTransition();
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

                builder.setSmallIcon(R.drawable.icon);
                builder.setContentTitle("어플 동작 로그");
                builder.setContentText("실내 지역에 입장하여서 무음으로 변경되었습니다.");
                builder.setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                }
                notificationManager.notify(100, builder.build());

                if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT)
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

                builder.setSmallIcon(R.drawable.icon);
                builder.setContentTitle("어플 동작 로그");
                builder.setContentText("실내 지역에 퇴장하여서 소리로 변경되었습니다.");
                builder.setAutoCancel(true);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                }
                notificationManager.notify(100, builder.build());
                if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
        } catch (NullPointerException e) {
            //AudioManager is expected to give NullPointerException sometime.
            Toast.makeText(context, "NullPointer Exception from AudioManager!!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //An Unknown Exception. Toast it.
            Toast.makeText(context, Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }
    }

    //Geofence error code 기반으로 적절한 에러 메시지 반환
    private String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "The Geofencing Api is not available now.";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "This application has too many geofences.";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pendingIntents are provided in addGeofences";
            default:
                return "An Unknown Error Occurred!";
        }
    }
}
