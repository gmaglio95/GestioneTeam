package it.maglio.gestioneUtenti.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.activity.MainActivity;
import it.maglio.gestioneUtenti.asyncTask.MqttAsyncTask;
import it.maglio.gestioneUtenti.mqtt.MqttClientUtils;
import it.maglio.gestioneUtenti.utility.PathUtils;

/**
 * Created by pc ads on 06/03/2017.
 */

public class MqttListnerService extends Service {

    private String IDDevice;
    private MqttAsyncTask taskToExecute;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String idDevice = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if(intent==null){
            intent = new Intent();
        }
        taskToExecute = new MqttAsyncTask(idDevice, this.getApplicationContext(), intent);
        taskToExecute.execute();
        return (START_STICKY);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
