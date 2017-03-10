package it.maglio.gestioneUtenti.asyncTask;

import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import it.maglio.gestioneUtenti.mqtt.MqttClientUtils;

/**
 * Created by pc ads on 06/03/2017.
 */

public class MqttAsyncTask extends AsyncTask<Void, Void, Void> {

    private Intent intent;
    private String idDevice;
    private Context context;

    public MqttAsyncTask(String idDevice, Context context, Intent intent) {
        this.idDevice = idDevice;
        this.context = context;
        this.intent = intent;
    }


    @Override
    protected Void doInBackground(Void... jobParameterses) {
        MqttClientUtils utils = new MqttClientUtils(idDevice, context, intent);
        utils.mosquittoConnection();
        return null;
    }


}
