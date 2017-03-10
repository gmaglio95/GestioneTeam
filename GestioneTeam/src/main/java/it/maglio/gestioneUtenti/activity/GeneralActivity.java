package it.maglio.gestioneUtenti.activity;

import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

import it.maglio.gestioneUtenti.utility.InterfaceToCallServer;
import it.maglio.gestioneUtenti.utility.MessageStringFactory;

/**
 * Created by pc ads on 23/02/2017.
 */

public abstract class GeneralActivity extends AppCompatActivity implements InterfaceToCallServer {


    public abstract void callBackAfterCall(Object object);

    public void showErrorServerConnection() {
        new MaterialDialog.Builder(this)
                .title(MessageStringFactory.SERVER_ERROR_HEADER)
                .content(MessageStringFactory.CONNESSIONE_NON_PRESENTE)
                .positiveText(MessageStringFactory.OK_MESSAGE).show();
    }

    public void showPopupWithMessage(String message) {
        new MaterialDialog.Builder(this)
                .title(MessageStringFactory.WARNING_HEADER)
                .content(message)
                .positiveText(MessageStringFactory.OK_MESSAGE).show();
    }

    public Object getInstance() {
        return this;
    }
}
