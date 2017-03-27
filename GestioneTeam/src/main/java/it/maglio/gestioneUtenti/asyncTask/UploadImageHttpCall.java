package it.maglio.gestioneUtenti.asyncTask;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.utility.PathUtils;

import static android.content.ContentValues.TAG;

/**
 * Created by pc ads on 27/03/2017.
 */

public class UploadImageHttpCall extends AsyncTask<Void, Integer, String> {


    private Bitmap imageToSend;
    private Activity activity;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private long totalSize;

    public UploadImageHttpCall(Bitmap bitmap, Activity activity) {
        this.imageToSend = bitmap;
        this.activity=activity;
    }

    @Override
    public void onPreExecute() {
        manager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(activity);
        builder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher);
    }

    @Override
    public void onProgressUpdate(Integer... update) {
        builder.setProgress(100, update[0], false);
        manager.notify(0, builder.build());
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.i(TAG, "Starting Upload...");
        final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });
            File file = new File(convertBitmapToString(imageToSend));
            entity.addPart("image", new FileBody(file));
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(PathUtils.URL + PathUtils.PORT + PathUtils.PATH_UPLOAD_PROFILE_IMAGE);
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
            String responseStr = EntityUtils.toString(response.getEntity());
            Log.i(TAG, "doFileUpload Response : " + responseStr);
        } catch (Exception e) {
            Log.e("exception", Log.getStackTraceString(e));
        }
        return "";
    }

    public String convertBitmapToString(Bitmap bmp) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            String imageStr = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            return imageStr;
        } catch (Exception e) {
            Log.e("Eccezione", Log.getStackTraceString(e));
        }
        return "";
    }

}
