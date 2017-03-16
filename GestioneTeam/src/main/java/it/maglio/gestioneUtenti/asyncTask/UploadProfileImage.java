package it.maglio.gestioneUtenti.asyncTask;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import it.maglio.gestioneUtenti.utility.InterfaceToCallServer;
import it.maglio.gestioneUtenti.utility.PathUtils;
import it.tortuga.beans.ErrorMessage;

/**
 * Created by pc ads on 13/03/2017.
 */

public class UploadProfileImage extends AsyncTask<Object, Integer, Boolean> {

    private String serverPath = PathUtils.URL + PathUtils.PORT + PathUtils.PATH_UPLOAD_PROFILE_IMAGE;
    private String filePath;
    private ErrorMessage message;
    private InterfaceToCallServer ui;


    public UploadProfileImage(String filePath) {
        this.filePath = filePath;

    }


    @Override
    protected Boolean doInBackground(Object... voids) {
        String fileName = filePath;
        Boolean sucess = false;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(filePath);

        if (!sourceFile.isFile()) {


            Log.e("uploadFile", "Source File not exist :"
                    + serverPath + "" + filePath);
            message = new ErrorMessage("Errore di caricamento del file");

        } else {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(serverPath);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name='uploaded_file';filename=''"
                        + fileName + "'" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                fileInputStream.close();
                dos.flush();
                dos.close();
                if (serverResponseCode == 200) {
                    sucess = true;
                }
            } catch (FileNotFoundException e) {
                Log.e("ERROR", Log.getStackTraceString(e));
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return sucess;
    }


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Integer... v) {
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            ui.showPopupWithMessage("Il File e' stato caricato con successo");
        }
    }


}