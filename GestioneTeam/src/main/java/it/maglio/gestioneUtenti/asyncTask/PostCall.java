package it.maglio.gestioneUtenti.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import it.maglio.gestioneUtenti.utility.InformationType;
import it.maglio.gestioneUtenti.utility.InterfaceToCallServer;
import it.tortuga.beans.GeneralBean;

/**
 * Created by pc ads on 22/02/2017.
 */
public class PostCall extends AsyncTask<GeneralBean, Void, GeneralBean> {

    private Gson gson;
    private InterfaceToCallServer activityAttuale;
    private String url;
    private String port;
    private String path;
    private ProgressDialog progressDialog;
    private InformationType type;
    private static final int HTTP_REQUEST_TIMEOUT = 5000;
    private int statusCode;
    String dialogMessage;

    public PostCall(String url, String port, String path, InterfaceToCallServer attuale, InformationType type, String dialogMessage) {
        this.gson = getGson();
        this.url = url;
        this.activityAttuale = attuale;
        this.port = port;
        this.path = path;
        this.dialogMessage = dialogMessage;
        this.type = type;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog((Context) activityAttuale.getInstance());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(dialogMessage);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected GeneralBean doInBackground(GeneralBean... params) {
        GeneralBean bean = null;
        if (params != null) {
            bean = params[0];
        }
        try {
            URL urlToConnect = new URL(url + port + path);
            HttpURLConnection conn = (HttpURLConnection) urlToConnect.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
            conn.getOutputStream().write(gson.toJson(bean).getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            GeneralBean beanRecived = (GeneralBean) gson.fromJson(br.readLine(), type.getClazz());
            return beanRecived;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    @Override
    protected void onPostExecute(GeneralBean result) {
        progressDialog.hide();
        if (result != null) {
            if (result.getErrorDescriptors() != null) {
                activityAttuale.showPopupWithMessage(result.getErrorDescriptors().getDescription());
            } else {
                activityAttuale.callBackAfterCall(result);
            }
        } else {
            activityAttuale.showErrorServerConnection();
        }
    }


    public static Gson getGson() {
        JsonSerializer<Date> ser = new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
                // TODO Auto-generated method stub
                return src == null ? null : new JsonPrimitive(src.getTime());
            }
        };

        JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
                return json == null ? null : new Date(json.getAsLong());
            }
        };

        return new GsonBuilder().registerTypeAdapter(Date.class, ser).registerTypeAdapter(Date.class, deser)
                .create();
    }
}
