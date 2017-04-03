package it.maglio.gestioneUtenti.asyncTask;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import it.giuseppe.app.R;
import it.maglio.gestioneUtenti.utility.MultipartUtility;
import it.maglio.gestioneUtenti.utility.PathUtils;
import it.tortuga.beans.ImageBean;
import it.tortuga.beans.User;

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
    private Uri uri;
    private HttpURLConnection httpConn;
    private ProgressDialog progressDialog;
    private int statusCode;

    public UploadImageHttpCall(Bitmap bitmap, Activity activity, Uri uri) {
        this.imageToSend = bitmap;
        this.activity = activity;
        this.uri = uri;
    }

    @Override
    public void onPreExecute() {
        progressDialog = new ProgressDialog((Context) activity);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Upload in corso...");
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    public void onProgressUpdate(Integer... update) {
        builder.setProgress(100, update[0], false);
        manager.notify(0, builder.build());
    }

    @Override
    public void onPostExecute(String statusCode) {

    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.i(TAG, "Starting Upload...");
        File file = new File(getRealPathFromURI(uri));

        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        if (file.exists()) {
            HttpClient client = new DefaultHttpClient();
            MultipartUtility utility = null;
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            FileBody body = new FileBody(file);
            builder.addPart("imgFile", body);
            builder.setCharset(Charset.defaultCharset());
            HttpPost post = new HttpPost(PathUtils.URL + PathUtils.PORT + PathUtils.PATH_UPLOAD_PROFILE_IMAGE);
            post.setEntity(builder.build());
            try {
                HttpResponse response = client.execute(post);
                response.getStatusLine().getStatusCode();
                response.getEntity();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public String convertBitmapToString(Bitmap bmp) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 50, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            String imageStr = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            return imageStr;
        } catch (Exception e) {
            Log.e("Eccezione", Log.getStackTraceString(e));
        }
        return "";
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
