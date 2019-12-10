package com.example.expriement3.backService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.SystemClock;

import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.expriement3.MainActivity;
import com.example.expriement3.R;
import com.example.expriement3.Weather;
import com.example.expriement3.WeatherLab;
import com.example.expriement3.todayWeather;
import com.example.expriement3.todayWeatherLab;
import com.example.expriement3.method;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollService extends android.app.IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL_MS =  TimeUnit.SECONDS.toMillis(60);
    private static final String PREF_LAST_RESULT_ID = "lastResultId";
    private static Context mcontext;
    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }
    public PollService() {
        super(TAG);

    }
    public static String getLastResultId(Context context) {  
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LAST_RESULT_ID, null);
    }
    public static void setLastResultId(Context context, String lastResultId) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_LAST_RESULT_ID, lastResultId).apply();
    }
        @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }

        Log.e("状态","我在通知" );
        String location="长沙";
        SharedPreferences pref=getSharedPreferences("data", MODE_PRIVATE);
        String unit;
        String msg =pref.getString("weatherunit","");
            if(msg.equals("英制")){
                unit="i";
            }
            else
                unit="m";
            List<Weather> weathers;
        String lastResultId = getLastResultId(this);
        weathers= WeatherLab.get(mcontext).getWeathers(location,unit);

        if (weathers.size() == 0) {
            return;
        }
        String resultId =weathers.get(0).getmId().toString();
        if (resultId.equals(lastResultId)) {
            Log.e(TAG, "Got an old result: " + resultId);
        }
        else {
            Log.e(TAG, "Got a new result: " + resultId);
            Intent i = MainActivity.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
            todayWeatherLab todayweatherLab=todayWeatherLab.get(mcontext);
            todayWeather todayweather=todayweatherLab.gettodayWeather(location,unit);
            String iconname="w"+todayweather.getCond_code();
            String id = "my_channel";
            String name="渠道";
            Notification notification = null;
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
                Toast.makeText(this, mChannel.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, mChannel.toString());

                notificationManager.createNotificationChannel(mChannel);
                Log.e("数据",todayweather.getTmp());
                notification = new Notification.Builder(this)
                        .setChannelId(id)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("温馨提示")
                        .setContentText("当前温度: "+todayweather.getTmp()+"℃   请做好保暖措施")
                        .setAutoCancel(true)
                        .setContentIntent(pi).build();
            }
            else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setTicker("234")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("温馨提示")
                        .setContentText("当前温度: "+todayweather.getTmp()+"℃   请做好保暖措施")
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setChannelId(id);//无效
                notification = notificationBuilder.build();
            }
            notificationManager.notify(111123, notification);

        }
        setLastResultId(this, resultId);

}
    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =    (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&    cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        mcontext= context;
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            Log.e("天气","已开启");
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,  SystemClock.elapsedRealtime(),POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
            Log.e("天气","已关闭");
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
}
