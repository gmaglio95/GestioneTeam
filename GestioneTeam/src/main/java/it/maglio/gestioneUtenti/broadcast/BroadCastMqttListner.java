package it.maglio.gestioneUtenti.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import it.maglio.gestioneUtenti.service.MqttListnerService;

import static android.content.ContentValues.TAG;

/**
 * Created by pc ads on 07/03/2017.
 */

public class BroadCastMqttListner extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.d("Informazione", "Il messaggio è arrivato");
            context.startService(new Intent(context, MqttListnerService.class));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
