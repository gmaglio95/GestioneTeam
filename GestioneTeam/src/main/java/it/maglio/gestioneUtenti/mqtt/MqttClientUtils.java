package it.maglio.gestioneUtenti.mqtt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.bean.SessionBean;
import it.maglio.gestioneUtenti.utility.PathUtils;
import it.tortuga.beans.User;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by pc ads on 03/03/2017.
 */

public class MqttClientUtils implements MqttCallback {


    private MqttClient myClient;
    MqttConnectOptions connOpt;
    static final String BROKER_URL = PathUtils.BROKER_MQTT + "1883";
    private String clientID;
    private Context context;
    private Intent intent;
    private int notificationCode;


    public MqttClientUtils(String clientID, Context context, Intent intent) {
        this.clientID = clientID;
        this.context = context;
        this.intent = intent;
        notificationCode = 0;
    }

    public void mosquittoConnection() {
        for (int i = 0; i < 10; i++) {
            if (!run()) {
                try {
                    Log.i("MQTT Connection :", "Connection lost to " + BROKER_URL);
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
    }


    private boolean run() {
        connOpt = new MqttConnectOptions();
        boolean connected = false;
        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);
        // Connect to Broker
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            myClient = new MqttClient(BROKER_URL, "Sending", persistence);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException e) {
            Log.e("ERROR", Log.getStackTraceString(e));
            return connected;
        }
        connected = true;
        System.out.println("Connected to " + BROKER_URL);

        // setup topic
        // topics on m2m.io are in the form <domain>/<stuff>/<thing>
        User userSession = SessionBean.getSessionBean().getUser();
        String userTopic = userSession.get_id();
        try {
            myClient.subscribe(userTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connected;
    }


    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost!");
        cause.printStackTrace();
        mosquittoConnection();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d("Message", message.toString());
        MqttMessage messageMqtt = new MqttMessage(message.toString().getBytes());
        MqttTopic mqttTopic = myClient.getTopic("team.2");
        mqttTopic.publish(messageMqtt);
        if (context == null) {
            Log.d("Cotext:", "null");
        }
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

        Notification n = new Notification.Builder(context).setContentTitle("Tortuga Volley")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setContentText(message.toString())
                .setSmallIcon(R.mipmap.small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setStyle(new Notification.BigTextStyle()
                        .bigText(message.toString()))
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        notificationManager.notify(notificationCode, n);
        notificationCode++;
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
